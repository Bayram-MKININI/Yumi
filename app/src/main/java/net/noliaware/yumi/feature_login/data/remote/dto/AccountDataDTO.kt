package net.noliaware.yumi.feature_login.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_categories.data.remote.dto.CategoryDTO
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_profile.data.remote.dto.UserProfileDTO

@JsonClass(generateAdapter = true)
data class AccountDataDTO(
    @Json(name = "encryptionVector")
    val encryptionVector: String = "",
    @Json(name = "messageSubjects")
    val messageSubjectDTOs: List<MessageSubjectDTO> = listOf(),
    @Json(name = "newAlertCount")
    val newAlertCount: Int = 0,
    @Json(name = "newMessageCount")
    val newMessageCount: Int = 0,
    @Json(name = "availableVoucherCountPerCategory")
    val categoryDTOs: List<CategoryDTO>? = listOf(),
    @Json(name = "managedAccounts")
    val managedAccountProfileDTOS: List<UserProfileDTO> = listOf()
) {
    fun toAccountData() = AccountData(
        messageSubjects = messageSubjectDTOs.map { it.toMessageSubject() },
        newAlertCount = newAlertCount,
        newMessageCount = newMessageCount,
        categories = categoryDTOs?.map { it.toCategory() },
        managedAccountProfiles = managedAccountProfileDTOS.map { it.toUserProfile() }
    )
}