package net.noliaware.yumi.feature_categories.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VouchersDTO(
    @Json(name = "categoryAvailableVoucherList")
    val voucherDTOList: List<VoucherDTO>
)