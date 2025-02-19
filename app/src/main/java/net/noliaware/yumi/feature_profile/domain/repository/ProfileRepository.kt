package net.noliaware.yumi.feature_profile.domain.repository

import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_profile.domain.model.BOSignIn
import net.noliaware.yumi.feature_profile.domain.model.UserProfile

interface ProfileRepository {

    fun getUserProfile(): Flow<Resource<UserProfile>>

    fun getBackOfficeSignInCode(): Flow<Resource<BOSignIn>>
}