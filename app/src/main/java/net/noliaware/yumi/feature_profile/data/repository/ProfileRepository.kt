package net.noliaware.yumi.feature_profile.data.repository

import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
import java.util.*

interface ProfileRepository {
    fun getUserProfile(): Flow<Resource<UserProfile>>

    fun getUsedVoucherList(categoryId: String): Flow<Resource<List<Voucher>>>
}