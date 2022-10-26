package net.noliaware.yumi.feature_categories.domain.model

import java.io.Serializable

data class VoucherCodeData(
    val productLabel: String?,
    val voucherDate: String?,
    val voucherExpiryDate: String?,
    val voucherCode: String?,
    val voucherCodeSize: Int
) : Serializable