package net.noliaware.yumi.feature_alerts.domain.model

import net.noliaware.yumi.commun.domain.model.Priority

data class Alert(
    val alertId: String,
    val alertDate: String,
    val alertTime: String,
    val alertPriority: Priority?,
    val alertText: String
)