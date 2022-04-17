package net.noliaware.yumi.feature_profile.data.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_categories.data.remote.dto.CategoryDTO
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_profile.domain.model.AccountProfile

@JsonClass(generateAdapter = true)
data class AccountProfileDTO(
    @Json(name = "login")
    val login: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String,
    @Json(name = "cellNumber")
    val cellNumber: String? = null,
    @Json(name = "availableVoucherCount")
    val availableVoucherCount: Int,
    @Json(name = "voucherCountPerCategory")
    val categories: List<CategoryDTO>
) {
    fun toProfile() = AccountProfile(
        login = login,
        title = title,
        firstName = firstName,
        lastName = lastName,
        cellNumber = cellNumber,
        availableVoucherCount = availableVoucherCount,
        categories = categories.map { it.toCategory() }
    )
}