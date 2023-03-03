package net.noliaware.yumi.feature_categories.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_categories.domain.model.VoucherStatus

@JsonClass(generateAdapter = true)
data class GetVoucherStatusDTO(
    @Json(name = "voucherStatus")
    val voucherStatus: Int,
) {
    fun toVoucherStatus() = when (voucherStatus) {
        0 -> VoucherStatus.CANCELLED
        1 -> VoucherStatus.USABLE
        2 -> VoucherStatus.CONSUMED
        else -> VoucherStatus.INEXISTENT
    }
}