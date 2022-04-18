package net.noliaware.yumi.feature_profile.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_categories.data.remote.dto.CategoryDTO
import net.noliaware.yumi.feature_profile.domain.model.UserProfile

@JsonClass(generateAdapter = true)
data class UserProfileDTO(
    @Json(name = "login")
    val login: String?,
    @Json(name = "userRef")
    val userRef: String?,
    @Json(name = "issuedVoucherCount")
    val issuedVoucherCount: Int = 0,
    @Json(name = "usedVoucherCount")
    val usedVoucherCount: Int = 0,
    @Json(name = "availableVoucherCount")
    val availableVoucherCount: Int = 0,
    @Json(name = "voucherCountPerCategory")
    val categories: List<CategoryDTO> = listOf(),
    @Json(name = "canceledVoucherCount")
    val canceledVoucherCount: Int = 0,
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
    @Json(name = "address")
    val address: String?,
    @Json(name = "addressComplement")
    val addressComplement: String?,
    @Json(name = "zipcode")
    val zipcode: String?,
    @Json(name = "city")
    val city: String?,
    @Json(name = "phoneNumber")
    val phoneNumber: String?,
    @Json(name = "cellPhoneNumber")
    val cellPhoneNumber: String?,
    @Json(name = "email")
    val email: String?
) {
    fun toUserProfile() = UserProfile(
        login = login,
        userRef = userRef,
        issuedVoucherCount = issuedVoucherCount,
        usedVoucherCount = usedVoucherCount,
        availableVoucherCount = availableVoucherCount,
        categories = categories.map { it.toCategory() },
        canceledVoucherCount = canceledVoucherCount,
        userJob = userJob,
        title = title,
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate,
        birthCity = birthCity,
        address = address,
        addressComplement = addressComplement,
        zipcode = zipcode,
        city = city,
        phoneNumber = phoneNumber,
        cellPhoneNumber = cellPhoneNumber,
        email = email
    )
}