package net.noliaware.yumi.feature_categories.data.repository

import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.domain.model.VoucherStatus

interface CategoryRepository {

    fun getVoucherList(categoryId: String): Flow<Resource<List<Voucher>>>

    fun getVoucherById(voucherId: String): Flow<Resource<Voucher>>

    fun getVoucherStatusById(voucherId: String): Flow<Resource<VoucherStatus>>
}