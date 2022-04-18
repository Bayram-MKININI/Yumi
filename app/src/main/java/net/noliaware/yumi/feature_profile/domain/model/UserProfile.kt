package net.noliaware.yumi.feature_profile.domain.model

import net.noliaware.yumi.feature_categories.domain.model.Category
import java.io.Serializable

data class UserProfile(
    val login: String?,
    val userRef: String?,
    val issuedVoucherCount: Int,
    val usedVoucherCount: Int,
    val availableVoucherCount: Int,
    var categories: List<Category> = listOf(),
    val canceledVoucherCount: Int,
    val userJob: String?,
    val title: String?,
    val firstName: String?,
    val lastName: String?,
    val birthDate: String?,
    val birthCity: String?,
    val address: String?,
    val addressComplement: String?,
    val zipcode: String?,
    val city: String?,
    val phoneNumber: String?,
    val cellPhoneNumber: String?,
    val email: String?
) : Serializable