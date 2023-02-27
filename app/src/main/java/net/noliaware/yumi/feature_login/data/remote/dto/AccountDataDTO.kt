package net.noliaware.yumi.feature_login.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_login.domain.model.AccountData

@JsonClass(generateAdapter = true)
data class AccountDataDTO(
    @Json(name = "welcomeMessage")
    val helloMessage: String = "",
    @Json(name = "welcomeUser")
    val userName: String = "",
    @Json(name = "availableVoucherCount")
    val availableVoucherCount: Int = 0,
    @Json(name = "encryptionVector")
    val encryptionVector: String = "",
    @Json(name = "messageSubjects")
    val messageSubjectDTOs: List<MessageSubjectDTO> = listOf(),
    @Json(name = "newAlertCount")
    val newAlertCount: Int = 0,
    @Json(name = "newMessageCount")
    val newMessageCount: Int = 0
) {
    fun toAccountData() = AccountData(
        helloMessage = helloMessage,
        userName = userName,
        availableVoucherCountSinceLast = availableVoucherCount,
        messageSubjects = messageSubjectDTOs.map { it.toMessageSubject() },
        newAlertCount = newAlertCount,
        newMessageCount = newMessageCount
    )
}