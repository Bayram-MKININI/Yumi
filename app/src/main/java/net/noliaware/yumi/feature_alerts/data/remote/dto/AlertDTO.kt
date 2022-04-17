package net.noliaware.yumi.feature_alerts.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_alerts.domain.model.Alert

@JsonClass(generateAdapter = true)
data class AlertDTO(
    @Json(name = "alertid")
    val alertId: Int,
    @Json(name = "alertdate")
    val alertDate: String,
    @Json(name = "alerttime")
    val alertTime: String,
    @Json(name = "alerttype")
    val alertType: String,
    @Json(name = "alertrank")
    val alertRank: Int,
    @Json(name = "alerttext")
    val alertText: String,
    @Json(name = "alerttotal")
    val alertTotal: Int
) {
    fun toAlert() = Alert(
        alertId = alertId,
        alertDate = alertDate,
        alertTime = alertTime,
        alertType = alertType,
        alertRank = alertRank,
        alertText = alertText,
        alertTotal = alertTotal
    )
}