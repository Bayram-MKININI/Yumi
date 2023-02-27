package net.noliaware.yumi.feature_categories.presentation.controllers

import android.content.Context
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.parseToShortDate
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.presentation.views.VoucherItemView.VoucherItemViewAdapter
import javax.inject.Inject

class AvailableVoucherMapper @Inject constructor() : VoucherMapper {

    override fun mapVoucher(
        context: Context,
        color: Int,
        voucher: Voucher
    ) = VoucherItemViewAdapter(
        color = color,
        title = voucher.productLabel.orEmpty(),
        highlightDescription = context.getString(R.string.expiry_date),
        highlightValue = parseToShortDate(voucher.voucherExpiryDate),
        retailer = context.getString(
            R.string.retailer_label,
            voucher.retailerLabel
        )
    )
}
