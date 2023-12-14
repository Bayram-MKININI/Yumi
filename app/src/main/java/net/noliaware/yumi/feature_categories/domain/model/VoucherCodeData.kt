package net.noliaware.yumi.feature_categories.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class VoucherCodeData(
    val voucherId: String?,
    val productLabel: String?,
    val voucherStartDate: String,
    val voucherEndDate: String,
    val voucherCode: String?,
    val voucherCodeSize: Int
) : Parcelable