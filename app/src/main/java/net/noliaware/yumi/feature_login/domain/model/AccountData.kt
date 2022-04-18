package net.noliaware.yumi.feature_login.domain.model

import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
import java.io.Serializable

data class AccountData(
    val messageSubjects: List<MessageSubject>,
    val newAlertCount: Int = 0,
    val newMessageCount: Int = 0,
    val categories: List<Category>,
    val managedAccountProfiles: List<UserProfile>
) : Serializable