package net.noliaware.yumi.feature_categories.presentation.mappers

import android.content.Context
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.DateTime.SHORT_DATE_FORMAT
import net.noliaware.yumi.commun.util.parseDateToFormat
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
        highlightValue = voucher.voucherExpiryDate?.parseDateToFormat(SHORT_DATE_FORMAT).orEmpty(),
        retailerDescription = context.getString(R.string.to_retrieve),
        retailerValue = voucher.retailerLabel
    )
}
