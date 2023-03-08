package net.noliaware.yumi.feature_profile.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_profile.domain.model.UserProfile

@JsonClass(generateAdapter = true)
data class UserProfileDTO(
    @Json(name = "login")
    val login: String?,
    @Json(name = "userRef")
    val userRef: String?,
    @Json(name = "userJob")
    val userJob: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "firstName")
    val firstName: String?,
    @Json(name = "lastName")
    val lastName: String?,
    @Json(name = "birthDate")
    val birthDate: String?,
    @Json(name = "birthCity")
    val birthCity: String?,
    @Json(name = "birthCountry")
    val birthCountry: String?,
    @Json(name = "address")
    val address: String?,
    @Json(name = "addressComplement")
    val addressComplement: String?,
    @Json(name = "postcode")
    val postCode: String?,
    @Json(name = "city")
    val city: String?,
    @Json(name = "country")
    val country: String?,
    @Json(name = "phoneNumber")
    val phoneNumber: String?,
    @Json(name = "cellPhoneNumber")
    val cellPhoneNumber: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "assignedVoucherCount")
    val assignedVoucherCount: Int,
    @Json(name = "availableVoucherCount")
    val availableVoucherCount: Int,
    @Json(name = "usedVoucherCount")
    val usedVoucherCount: Int,
    @Json(name = "canceledVoucherCount")
    val cancelledVoucherCount: Int,
    @Json(name = "messageBoxUsagePercentage")
    val messageBoxUsagePercentage: Int
) {
    fun toUserProfile() = UserProfile(
        login = login,
        userReferent = userRef,
        userJob = userJob,
        title = title,
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate,
        birthCity = birthCity,
        birthCountry = birthCountry,
        address = address,
        addressComplement = addressComplement,
        postCode = postCode,
        city = city,
        country = country,
        phoneNumber = phoneNumber,
        cellPhoneNumber = cellPhoneNumber,
        email = email,
        assignedVoucherCount = assignedVoucherCount,
        availableVoucherCount = availableVoucherCount,
        usedVoucherCount = usedVoucherCount,
        cancelledVoucherCount = cancelledVoucherCount,
        messageBoxUsagePercentage = messageBoxUsagePercentage,
    )
}