package net.noliaware.yumi.feature_profile.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.noliaware.yumi.commun.*
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.ErrorType
import net.noliaware.yumi.commun.util.generateToken
import net.noliaware.yumi.commun.util.getCommonWSParams
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import java.util.*
import javax.inject.Inject

class UsedVoucherPagingSource @Inject constructor(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : PagingSource<Int, Voucher>() {

    lateinit var categoryId: String

    override fun getRefreshKey(state: PagingState<Int, Voucher>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Voucher> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPage = params.key ?: 0

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchUsedVouchersForCategory(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    GET_USED_VOUCHER_LIST_BY_CATEGORY,
                    randomString
                ),
                params = generateWSParams(categoryId, nextPage)
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

            val voucherRank = remoteData.data?.voucherDTOList?.last()?.voucherRank ?: nextPage

            val moreItemsAvailable = remoteData.data?.voucherDTOList?.last()?.let { voucherDTO ->
                if (voucherDTO.voucherRank != null && voucherDTO.voucherCount != null) {
                    voucherDTO.voucherRank < voucherDTO.voucherCount
                } else {
                    false
                }
            }

            val canLoadMore = moreItemsAvailable == true && errorType != ErrorType.SYSTEM_ERROR

            return LoadResult.Page(
                data = remoteData.data?.voucherDTOList?.map { it.toVoucher() }.orEmpty(),
                prevKey = null,// Only paging forward.
                nextKey = if (canLoadMore) voucherRank else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private fun generateWSParams(categoryId: String, offset: Int) = mutableMapOf(
        CATEGORY_ID to categoryId,
        LIMIT to LIST_PAGE_SIZE.toString(),
        OFFSET to offset.toString()
    ).also {
        it.plusAssign(getCommonWSParams(sessionData))
    }.toMap()
}