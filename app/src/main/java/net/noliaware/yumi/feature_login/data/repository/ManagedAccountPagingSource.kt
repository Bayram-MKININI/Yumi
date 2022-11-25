package net.noliaware.yumi.feature_login.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.noliaware.yumi.commun.GET_MANAGED_ACCOUNT_LIST
import net.noliaware.yumi.commun.LIMIT
import net.noliaware.yumi.commun.LIST_PAGE_SIZE
import net.noliaware.yumi.commun.OFFSET
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.ErrorType
import net.noliaware.yumi.commun.util.generateToken
import net.noliaware.yumi.commun.util.getCommonWSParams
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
import java.util.*
import javax.inject.Inject

class ManagedAccountPagingSource @Inject constructor(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : PagingSource<Int, UserProfile>() {

    override fun getRefreshKey(state: PagingState<Int, UserProfile>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserProfile> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPage = params.key ?: 0

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchManagedAccounts(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    GET_MANAGED_ACCOUNT_LIST,
                    randomString
                ),
                params = generateWSParams(nextPage)
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

            val profileRank =
                remoteData.data?.managedAccountProfileDTOS?.last()?.profileRank ?: nextPage

            val moreItemsAvailable =
                remoteData.data?.managedAccountProfileDTOS?.last()?.let { profileDTO ->
                    if (profileDTO.profileRank != null && profileDTO.profileCount != null) {
                        profileDTO.profileRank < profileDTO.profileCount
                    } else {
                        false
                    }
                }

            val canLoadMore = moreItemsAvailable == true && errorType != ErrorType.SYSTEM_ERROR

            return LoadResult.Page(
                data = remoteData.data?.managedAccountProfileDTOS?.map { it.toUserProfile() }
                    .orEmpty(),
                prevKey = null,// Only paging forward.
                nextKey = if (canLoadMore) profileRank else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private fun generateWSParams(offset: Int) = mutableMapOf(
        LIMIT to LIST_PAGE_SIZE.toString(),
        OFFSET to offset.toString()
    ).also {
        it.plusAssign(getCommonWSParams(sessionData))
    }.toMap()
}