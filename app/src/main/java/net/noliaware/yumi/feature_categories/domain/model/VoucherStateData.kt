package net.noliaware.yumi.feature_categories.domain.model

data class VoucherStateData(
    val voucherStatus: VoucherStatus?,
    val voucherUseDate: String?,
    val voucherUseTime: String?
)