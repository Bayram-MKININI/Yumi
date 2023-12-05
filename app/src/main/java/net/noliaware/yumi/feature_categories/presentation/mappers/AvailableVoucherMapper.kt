package net.noliaware.yumi.feature_categories.presentation.mappers

import android.content.Context
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.DateTime.SHORT_DATE_FORMAT
import net.noliaware.yumi.commun.util.DecoratedText
import net.noliaware.yumi.commun.util.decorateWords
import net.noliaware.yumi.commun.util.getFontFromResources
import net.noliaware.yumi.commun.util.parseDateToFormat
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.domain.model.VoucherDeliveryStatus
import net.noliaware.yumi.feature_categories.presentation.views.VoucherItemView.VoucherItemViewAdapter
import javax.inject.Inject

class AvailableVoucherMapper @Inject constructor() : VoucherMapper {

    override fun mapVoucher(
        context: Context,
        color: Int,
        voucher: Voucher
    ) = VoucherItemViewAdapter(
        isDeactivated = voucher.voucherDeliveryStatus != VoucherDeliveryStatus.AVAILABLE,
        color = color,
        hasOngoingRequests = voucher.voucherOngoingRequestCount > 0,
        title = voucher.productLabel.orEmpty(),
        highlight = mapHighlight(context, voucher),
        retailerDescription = context.getString(R.string.to_retrieve),
        retailerValue = voucher.retailerLabel
    )

    private fun mapHighlight(
        context: Context,
        voucher: Voucher
    ) = if (voucher.voucherExpiryDate != null) {
        val expiryDate = voucher.voucherExpiryDate.parseDateToFormat(SHORT_DATE_FORMAT)
        context.getString(
            R.string.usable_end_date,
            expiryDate
        ).decorateWords(
            wordsToDecorate = listOf(
                DecoratedText(
                    textToDecorate = expiryDate,
                    typeface = context.getFontFromResources(R.font.omnes_semibold_regular)
                )
            )
        )
    } else {
        val startDate = voucher.voucherStartDate?.parseDateToFormat(SHORT_DATE_FORMAT).orEmpty()
        context.getString(
            R.string.usable_start_date,
            startDate
        ).decorateWords(
            wordsToDecorate = listOf(
                DecoratedText(
                    textToDecorate = startDate,
                    typeface = context.getFontFromResources(R.font.omnes_semibold_regular)
                )
            )
        )
    }
}