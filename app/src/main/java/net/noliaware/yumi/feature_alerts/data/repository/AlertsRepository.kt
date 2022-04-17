package net.noliaware.yumi.feature_alerts.data.repository

import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_alerts.domain.model.Alert

interface AlertsRepository {
    fun getAlertList(): Flow<Resource<List<Alert>>>
}