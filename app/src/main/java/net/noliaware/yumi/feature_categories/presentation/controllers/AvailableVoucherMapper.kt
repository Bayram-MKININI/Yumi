package net.noliaware.yumi.feature_categories.presentation.controllers

import android.content.Context
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.parseToShortDate
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.presentation.views.VoucherItemView
import javax.inject.Inject

class AvailableVoucherMapper @Inject constructor() : VoucherMapper {

    override fun mapVoucher(
        context: Context, voucher: Voucher
    ) = VoucherItemView.VoucherItemViewAdapter(
        title = voucher.productLabel.orEmpty(),
        expiryDate = context.getString(
            R.string.expiry_date,
            parseToShortDate(voucher.voucherExpiryDate)
        ),
        description = context.getString(
            R.string.retailer,
            voucher.retailerLabel
        )
    )
}
