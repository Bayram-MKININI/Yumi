package net.noliaware.yumi.feature_categories.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetVoucherStateDataDTO(
    @Json(name = "voucherData")
    val voucherStateData: GetVoucherStatusDTO
)