package net.noliaware.yumi.feature_categories.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.domain.model.VoucherStateData

interface CategoryRepository {
    fun updatePrivacyPolicyReadStatus(): Flow<Resource<Boolean>>
    fun getAvailableCategories(): Flow<Resource<List<Category>>>
    fun getAvailableVoucherList(categoryId: String): Flow<PagingData<Voucher>>
    fun getUsedCategories(): Flow<Resource<List<Category>>>
    fun getUsedVoucherList(categoryId: String): Flow<PagingData<Voucher>>
    fun getCancelledCategories(): Flow<Resource<List<Category>>>
    fun getCancelledVoucherList(categoryId: String): Flow<PagingData<Voucher>>
    fun getVoucherById(voucherId: String): Flow<Resource<Voucher>>
    fun getVoucherStateDataById(voucherId: String): Flow<Resource<VoucherStateData>>
    fun useVoucherById(voucherId: String): Flow<Resource<Boolean>>
}