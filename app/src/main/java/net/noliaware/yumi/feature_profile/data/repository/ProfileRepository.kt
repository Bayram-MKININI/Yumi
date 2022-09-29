package net.noliaware.yumi.feature_profile.data.repository

import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_profile.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getUserProfile(): Resource<UserProfile>

    fun getUsedVoucherList(categoryId: String): Flow<Resource<List<Voucher>>>
}