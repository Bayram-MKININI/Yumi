package net.noliaware.yumi.feature_alerts.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.noliaware.yumi.commun.GET_ALERT_LIST
import net.noliaware.yumi.commun.LIMIT
import net.noliaware.yumi.commun.LIST_PAGE_SIZE
import net.noliaware.yumi.commun.TIMESTAMP_OFFSET
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.ErrorType
import net.noliaware.yumi.commun.util.generateToken
import net.noliaware.yumi.commun.util.getCommonWSParams
import net.noliaware.yumi.feature_alerts.domain.model.Alert
import java.util.*
import javax.inject.Inject

class AlertPagingSource @Inject constructor(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : PagingSource<Long, Alert>() {

    override fun getRefreshKey(state: PagingState<Long, Alert>): Nothing? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Alert> {
        try {
            // Start refresh at page 1 if undefined.
            val nextTimestamp = params.key ?: 0

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchAlertList(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    GET_ALERT_LIST,
                    randomString
                ),
                params = generateGetAlertsListParams(nextTimestamp)
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

            val alertTimestamp = remoteData.data?.alertDTOList?.last()?.alertTimestamp ?: nextTimestamp

            val moreItemsAvailable = remoteData.data?.alertDTOList?.last()?.let { alertDTO ->
                alertDTO.alertRank < alertDTO.alertCount
            }

            val canLoadMore = moreItemsAvailable == true && errorType != ErrorType.SYSTEM_ERROR

            return LoadResult.Page(
                data = remoteData.data?.alertDTOList?.map { it.toAlert() }.orEmpty(),
                prevKey = null,// Only paging forward.
                nextKey = if (canLoadMore) alertTimestamp else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private fun generateGetAlertsListParams(timestamp: Long) = mutableMapOf(
        TIMESTAMP_OFFSET to timestamp.toString(),
        LIMIT to LIST_PAGE_SIZE.toString()
    ).also {
        it.plusAssign(getCommonWSParams(sessionData))
    }.toMap()
}