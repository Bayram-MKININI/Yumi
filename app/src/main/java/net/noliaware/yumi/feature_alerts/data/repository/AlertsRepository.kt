package net.noliaware.yumi.feature_alerts.data.repository

import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_alerts.domain.model.Alert

interface AlertsRepository {
    suspend fun getAlertList(): Resource<List<Alert>>
}