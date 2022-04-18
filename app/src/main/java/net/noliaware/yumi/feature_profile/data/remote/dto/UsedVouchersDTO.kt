package net.noliaware.yumi.feature_profile.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_categories.data.remote.dto.VoucherDTO

@JsonClass(generateAdapter = true)
data class UsedVouchersDTO(
    @Json(name = "categoryUsedVouchers")
    val voucherDTOList: List<VoucherDTO>
)