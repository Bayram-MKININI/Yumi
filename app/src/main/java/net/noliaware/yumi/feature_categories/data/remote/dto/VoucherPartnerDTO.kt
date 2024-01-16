package net.noliaware.yumi.feature_categories.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_categories.domain.model.VoucherPartner

@JsonClass(generateAdapter = true)
data class VoucherPartnerDTO(
    @Json(name = "partnerInfoText")
    val partnerInfoText: String?,
    @Json(name = "partnerInfoURL")
    val partnerInfoURL: String?
) {
    fun toVoucherPartner() = VoucherPartner(
        partnerInfoText = partnerInfoText,
        partnerInfoURL = partnerInfoURL
    )
}