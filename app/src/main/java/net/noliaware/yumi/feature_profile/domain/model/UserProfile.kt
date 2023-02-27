package net.noliaware.yumi.feature_profile.domain.model

import java.io.Serializable

data class UserProfile(
    val login: String?,
    val userReferent: String?,
    val userJob: String?,
    val title: String?,
    val firstName: String?,
    val lastName: String?,
    val birthDate: String?,
    val birthCity: String?,
    val birthCountry: String?,
    val address: String?,
    val addressComplement: String?,
    val postCode: String?,
    val city: String?,
    val country: String?,
    val phoneNumber: String?,
    val cellPhoneNumber: String?,
    val email: String?,
    val messageBoxUsagePercentage: Int
) : Serializable