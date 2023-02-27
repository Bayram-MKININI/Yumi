package net.noliaware.yumi.feature_categories.presentation.controllers

import android.content.Context
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.presentation.views.VoucherItemView

interface VoucherMapper {
    fun mapVoucher(
        context: Context,
        color: Int,
        voucher: Voucher
    ): VoucherItemView.VoucherItemViewAdapter
}