package net.noliaware.yumi.feature_alerts.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.commun.domain.model.Priority
import net.noliaware.yumi.feature_alerts.domain.model.Alert

@JsonClass(generateAdapter = true)
data class AlertDTO(
    @Json(name = "alertId")
    val alertId: String,
    @Json(name = "alertDate")
    val alertDate: String,
    @Json(name = "alertTime")
    val alertTime: String,
    @Json(name = "alertTimestamp")
    val alertTimestamp: Long,
    @Json(name = "alertLevel")
    val alertLevel: Int,
    @Json(name = "alertText")
    val alertText: String,
    @Json(name = "alertRank")
    val alertRank: Int,
    @Json(name = "alertCount")
    val alertCount: Int
) {
    fun toAlert() = Alert(
        alertId = alertId,
        alertDate = alertDate,
        alertTime = alertTime,
        alertPriority = Priority.fromValue(alertLevel),
        alertText = alertText
    )
}