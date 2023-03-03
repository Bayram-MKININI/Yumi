package net.noliaware.yumi.feature_profile.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_profile.domain.model.BOSignIn
import net.noliaware.yumi.feature_profile.domain.model.UserProfile

interface ProfileRepository {
    fun getUserProfile(): Flow<Resource<UserProfile>>
    fun getBackOfficeSignInCode(): Flow<Resource<BOSignIn>>
    fun getUsedCategories(): Flow<Resource<List<Category>>>
    fun getUsedVoucherList(categoryId: String): Flow<PagingData<Voucher>>
    fun getCancelledCategories(): Flow<Resource<List<Category>>>
    fun getCancelledVoucherList(categoryId: String): Flow<PagingData<Voucher>>
}