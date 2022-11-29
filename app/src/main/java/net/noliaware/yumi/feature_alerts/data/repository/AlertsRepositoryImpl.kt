package net.noliaware.yumi.feature_alerts.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.LIST_PAGE_SIZE
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.feature_alerts.domain.model.Alert

class AlertsRepositoryImpl(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : AlertsRepository {

    override fun getAlertList(): Flow<PagingData<Alert>> = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        AlertPagingSource(api, sessionData)
    }.flow
}