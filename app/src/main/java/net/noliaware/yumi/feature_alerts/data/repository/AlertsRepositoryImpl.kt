package net.noliaware.yumi.feature_alerts.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import net.noliaware.yumi.commun.ApiParameters.LIST_PAGE_SIZE
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.feature_alerts.domain.repository.AlertsRepository

class AlertsRepositoryImpl(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : AlertsRepository {

    override fun getAlertList() = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        AlertPagingSource(api, sessionData)
    }.flow
}