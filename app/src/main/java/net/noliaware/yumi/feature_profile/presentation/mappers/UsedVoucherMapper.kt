package net.noliaware.yumi.feature_profile.presentation.mappers

import android.content.Context
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.parseToShortDate
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.presentation.mappers.VoucherMapper
import net.noliaware.yumi.feature_categories.presentation.views.VoucherItemView.VoucherItemViewAdapter
import javax.inject.Inject

class UsedVoucherMapper @Inject constructor() : VoucherMapper {

    override fun mapVoucher(
        context: Context,
        color: Int,
        voucher: Voucher
    ) = VoucherItemViewAdapter(
        color = color,
        title = voucher.productLabel.orEmpty(),
        highlightDescription = context.getString(R.string.validation_date),
        highlightValue = parseToShortDate(voucher.voucherUseDate),
        retailer = context.getString(
            R.string.retailer_label,
            voucher.retailerLabel
        )
    )
}
