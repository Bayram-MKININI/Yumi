package net.noliaware.yumi.feature_alerts.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_alerts.domain.model.Alert

@JsonClass(generateAdapter = true)
data class AlertDTO(
    @Json(name = "alertId")
    val alertId: String,
    @Json(name = "alertDate")
    val alertDate: String,
    @Json(name = "alertTime")
    val alertTime: String,
    @Json(name = "alertLevel")
    val alertLevel: Int,
    @Json(name = "alertRank")
    val alertRank: Int,
    @Json(name = "alertText")
    val alertText: String,
    @Json(name = "alertTotal")
    val alertTotal: Int
) {
    fun toAlert() = Alert(
        alertId = alertId,
        alertDate = alertDate,
        alertTime = alertTime,
        alertLevel = alertLevel,
        alertRank = alertRank,
        alertText = alertText,
        alertTotal = alertTotal
    )
}