package net.noliaware.yumi.feature_login.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_login.domain.model.TFAMode

@JsonClass(generateAdapter = true)
data class AccountDataDTO(
    @Json(name = "privacyPolicyUrl")
    val privacyPolicyUrl: String = "",
    @Json(name = "privacyPolicyReadStatus")
    val privacyPolicyReadStatus: Int,
    @Json(name = "welcomeMessage")
    val helloMessage: String = "",
    @Json(name = "welcomeUser")
    val userName: String = "",
    @Json(name = "availableVoucherCount")
    val availableVoucherCount: Int = 0,
    @Json(name = "voucherRequestTypes")
    val voucherRequestTypeDTOs: List<VoucherRequestTypeDTO> = listOf(),
    @Json(name = "encryptionVector")
    val encryptionVector: String = "",
    @Json(name = "messageSubjects")
    val messageSubjectDTOs: List<MessageSubjectDTO> = listOf(),
    @Json(name = "newAlertCount")
    val newAlertCount: Int = 0,
    @Json(name = "newMessageCount")
    val newMessageCount: Int = 0,
    @Json(name = "twoFactorAuthMode")
    val twoFactorAuthMode: Int = 0
) {
    fun toAccountData() = AccountData(
        privacyPolicyUrl = privacyPolicyUrl,
        shouldConfirmPrivacyPolicy = privacyPolicyReadStatus == 0,
        helloMessage = helloMessage,
        userName = userName,
        availableVoucherCount = availableVoucherCount,
        voucherRequestTypes = voucherRequestTypeDTOs.map { it.toVoucherRequestType() },
        messageSubjects = messageSubjectDTOs.map { it.toMessageSubject() },
        newAlertCount = newAlertCount,
        newMessageCount = newMessageCount,
        twoFactorAuthMode = TFAMode.fromInt(twoFactorAuthMode) ?: TFAMode.NONE
    )
}