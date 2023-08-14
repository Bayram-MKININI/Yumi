package net.noliaware.yumi.feature_categories.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.noliaware.yumi.commun.CATEGORY_ID
import net.noliaware.yumi.commun.GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY
import net.noliaware.yumi.commun.LIMIT
import net.noliaware.yumi.commun.LIST_PAGE_SIZE
import net.noliaware.yumi.commun.OFFSET
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.ErrorType
import net.noliaware.yumi.commun.util.PaginationException
import net.noliaware.yumi.commun.util.generateToken
import net.noliaware.yumi.commun.util.getCommonWSParams
import net.noliaware.yumi.commun.util.handlePaginatedListErrorIfAny
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import java.util.UUID

class CancelledVoucherPagingSource(
    private val api: RemoteApi,
    private val sessionData: SessionData,
    private val categoryId: String
) : PagingSource<Int, Voucher>() {

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

            val remoteData = api.fetchCancelledVouchersForCategory(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY,
                    randomString = randomString
                ),
                params = generateWSParams(
                    categoryId,
                    nextPage,
                    GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY
                )
            )

            val errorType = handlePaginatedListErrorIfAny(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY
            )

            if (errorType != ErrorType.RECOVERABLE_ERROR) {
                throw PaginationException(errorType)
            }

            val voucherRank = remoteData.data?.voucherDTOList?.lastOrNull()?.voucherRank ?: nextPage

            val moreItemsAvailable = remoteData.data?.voucherDTOList?.lastOrNull()?.let { voucherDTO ->
                    if (voucherDTO.voucherRank != null && voucherDTO.voucherCount != null) {
                        voucherDTO.voucherRank < voucherDTO.voucherCount
                    } else {
                        false
                    }
                }

            val canLoadMore = moreItemsAvailable == true

            return LoadResult.Page(
                data = remoteData.data?.voucherDTOList?.map { it.toVoucher() }.orEmpty(),
                prevKey = null,// Only paging forward.
                nextKey = if (canLoadMore) voucherRank else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private fun generateWSParams(
        categoryId: String,
        offset: Int,
        tokenKey: String
    ) = mutableMapOf(
        CATEGORY_ID to categoryId,
        LIMIT to LIST_PAGE_SIZE.toString(),
        OFFSET to offset.toString()
    ).also {
        it.plusAssign(getCommonWSParams(sessionData, tokenKey))
    }.toMap()
}