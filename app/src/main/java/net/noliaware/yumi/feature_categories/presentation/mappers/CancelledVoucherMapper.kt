package net.noliaware.yumi.feature_categories.presentation.mappers

import android.content.Context
import android.text.SpannableString
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.DateTime
import net.noliaware.yumi.commun.DateTime.SHORT_DATE_FORMAT
import net.noliaware.yumi.commun.util.DecoratedText
import net.noliaware.yumi.commun.util.decorateWords
import net.noliaware.yumi.commun.util.getFontFromResources
import net.noliaware.yumi.commun.util.parseDateToFormat
import net.noliaware.yumi.commun.util.parseTimeToFormat
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.presentation.views.VoucherItemView.VoucherItemViewAdapter
import javax.inject.Inject

class CancelledVoucherMapper @Inject constructor() : VoucherMapper {

    override fun mapVoucher(
        context: Context,
        color: Int,
        voucher: Voucher
    ) = VoucherItemViewAdapter(
        color = color,
        highlight = mapHighlight(context, voucher),
        title = voucher.productLabel.orEmpty(),
        retailerDescription = context.getString(R.string.retailer),
        retailerValue = voucher.retailerLabel
    )

    private fun mapHighlight(
        context: Context,
        voucher: Voucher
    ): SpannableString {
        val cancellationDate = voucher.voucherUseDate?.parseDateToFormat(SHORT_DATE_FORMAT)
        val cancellationTime = voucher.voucherUseTime?.parseTimeToFormat(DateTime.HOURS_TIME_FORMAT)
        return context.getString(
            R.string.cancellation_date,
            context.getString(R.string.date_time, cancellationDate, cancellationTime)
        ).decorateWords(
            wordsToDecorate = listOf(
                DecoratedText(
                    textToDecorate = cancellationDate.orEmpty(),
                    typeface = context.getFontFromResources(R.font.omnes_semibold_regular)
                ),
                DecoratedText(
                    textToDecorate = cancellationTime.orEmpty(),
                    typeface = context.getFontFromResources(R.font.omnes_semibold_regular)
                )
            )
        )
    }
}