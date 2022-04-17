package net.noliaware.yumi.feature_alerts.domain.model

data class Alert(
    val alertId: Int,
    val alertDate: String,
    val alertTime: String,
    val alertType: String,
    val alertRank: Int,
    val alertText: String,
    val alertTotal: Int
)