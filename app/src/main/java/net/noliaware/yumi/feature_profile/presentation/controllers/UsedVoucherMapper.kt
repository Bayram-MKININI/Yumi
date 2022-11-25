package net.noliaware.yumi.feature_profile.presentation.controllers

import android.content.Context
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.parseTimeString
import net.noliaware.yumi.commun.util.parseToShortDate
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.presentation.controllers.VoucherMapper
import net.noliaware.yumi.feature_categories.presentation.views.VoucherItemView
import javax.inject.Inject

class UsedVoucherMapper @Inject constructor() : VoucherMapper {

    override fun mapVoucher(
        context: Context, voucher: Voucher
    ) = VoucherItemView.VoucherItemViewAdapter(
        title = voucher.productLabel.orEmpty(),
        expiryDate = context.getString(
            R.string.usage_date,
            parseToShortDate(voucher.voucherUseDate),
            parseTimeString(voucher.voucherUseTime)
        ),
        description = context.getString(
            R.string.retailer,
            voucher.retailerLabel
        )
    )
}
