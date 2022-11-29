package net.noliaware.yumi.feature_message.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.noliaware.yumi.commun.GET_OUTBOX_MESSAGE_LIST
import net.noliaware.yumi.commun.LIMIT
import net.noliaware.yumi.commun.LIST_PAGE_SIZE
import net.noliaware.yumi.commun.OFFSET
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.ErrorType
import net.noliaware.yumi.commun.util.generateToken
import net.noliaware.yumi.commun.util.getCommonWSParams
import net.noliaware.yumi.feature_message.domain.model.Message
import java.util.*

class OutboxMessagePagingSource(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : PagingSource<Int, Message>() {

    override fun getRefreshKey(state: PagingState<Int, Message>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPage = params.key ?: 0

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchOutboxMessages(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    GET_OUTBOX_MESSAGE_LIST,
                    randomString
                ),
                params = generateGetMessagesListParams(nextPage)
            )

            val errorType = remoteData.session?.let { sessionDTO ->
                sessionData.apply {
                    sessionId = sessionDTO.sessionId
                    sessionToken = sessionDTO.sessionToken
                }

                ErrorType.RECOVERABLE_ERROR
            } ?: run {
                ErrorType.SYSTEM_ERROR
            }

            val messageRank = remoteData.data?.messageDTOList?.last()?.messageRank ?: nextPage

            val moreItemsAvailable = remoteData.data?.messageDTOList?.last()?.let { messageDTO ->
                if (messageDTO.messageRank != null && messageDTO.messageCount != null) {
                    messageDTO.messageRank < messageDTO.messageCount
                } else {
                    false
                }
            }

            val canLoadMore = moreItemsAvailable == true && errorType != ErrorType.SYSTEM_ERROR

            return LoadResult.Page(
                data = remoteData.data?.messageDTOList?.map { it.toMessage() }.orEmpty(),
                prevKey = null,// Only paging forward.
                nextKey = if (canLoadMore) messageRank else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private fun generateGetMessagesListParams(offset: Int) = mutableMapOf(
        LIMIT to LIST_PAGE_SIZE.toString(),
        OFFSET to offset.toString()
    ).also {
        it.plusAssign(getCommonWSParams(sessionData))
    }.toMap()
}