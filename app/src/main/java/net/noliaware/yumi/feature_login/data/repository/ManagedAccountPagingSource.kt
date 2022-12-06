package net.noliaware.yumi.feature_login.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.noliaware.yumi.commun.GET_MANAGED_ACCOUNT_LIST
import net.noliaware.yumi.commun.LIMIT
import net.noliaware.yumi.commun.LIST_PAGE_SIZE
import net.noliaware.yumi.commun.OFFSET
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
import java.util.*

class ManagedAccountPagingSource(
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
                    timestamp = timestamp,
                    methodName = GET_MANAGED_ACCOUNT_LIST,
                    randomString = randomString
                ),
                params = generateWSParams(nextPage, GET_MANAGED_ACCOUNT_LIST)
            )

            val errorType = handlePaginatedListErrorIfAny(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_MANAGED_ACCOUNT_LIST
            )

            if (errorType != ErrorType.RECOVERABLE_ERROR) {
                throw PaginationException(errorType)
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

            val canLoadMore = moreItemsAvailable == true

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

    private fun generateWSParams(offset: Int, tokenKey: String) = mutableMapOf(
        LIMIT to LIST_PAGE_SIZE.toString(),
        OFFSET to offset.toString()
    ).also {
        it.plusAssign(getCommonWSParams(sessionData, tokenKey))
    }.toMap()
}