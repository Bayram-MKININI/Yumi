package net.noliaware.yumi.feature_login.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_categories.data.remote.dto.CategoryDTO
import net.noliaware.yumi.feature_login.domain.model.AccountData

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
    @Json(name = "accountManager")
    val accountManager: Int?
) {
    fun toAccountData() = AccountData(
        messageSubjects = messageSubjectDTOs.map { it.toMessageSubject() },
        newAlertCount = newAlertCount,
        newMessageCount = newMessageCount,
        categories = categoryDTOs?.map { it.toCategory() },
        isAccountManager = accountManager == 1
    )
}