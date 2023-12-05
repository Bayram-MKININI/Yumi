package net.noliaware.yumi.feature_categories.domain.model

data class VoucherRequest(
    val voucherRequestId: String,
    val voucherRequestStatus: Int?,
    val voucherRequestDate: String?,
    val voucherRequestTime: String?,
    val voucherRequestLabel: String?,
    val voucherRequestComment: String?
)