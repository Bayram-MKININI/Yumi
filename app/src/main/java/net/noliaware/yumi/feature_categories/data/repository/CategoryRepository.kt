package net.noliaware.yumi.feature_categories.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.domain.model.VoucherStatus

interface CategoryRepository {

    fun getAvailableCategories(): Flow<Resource<List<Category>>>
    fun getAvailableVoucherList(categoryId: String): Flow<PagingData<Voucher>>
    fun getUsedCategories(): Flow<Resource<List<Category>>>
    fun getUsedVoucherList(categoryId: String): Flow<PagingData<Voucher>>
    fun getCancelledCategories(): Flow<Resource<List<Category>>>
    fun getCancelledVoucherList(categoryId: String): Flow<PagingData<Voucher>>
    fun getVoucherById(voucherId: String): Flow<Resource<Voucher>>
    fun getVoucherStatusById(voucherId: String): Flow<Resource<VoucherStatus>>
    fun useVoucherById(voucherId: String): Flow<Resource<Boolean>>
}