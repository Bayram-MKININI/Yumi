package net.noliaware.yumi.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.domain.model.ConnectData

@JsonClass(generateAdapter = true)
data class ConnectDTO(
    @Json(name = "encryptionVector")
    val encryptionVector: String = "",
    @Json(name = "messageSubjects")
    val messageSubjectDTOs: List<MessageSubjectDTO> = listOf(),
    @Json(name = "newAlertCount")
    val newAlertCount: Int = 0,
    @Json(name = "newMessageCount")
    val newMessageCount: Int = 0,
    @Json(name = "voucherCountPerCategory")
    val categoryDTOs: List<CategoryDTO> = listOf(),
    @Json(name = "managedAccounts")
    val managedAccountDTOs: List<ManagedAccountDTO> = listOf()
) {
    fun toConnectData() = ConnectData(
        messageSubjects = messageSubjectDTOs.map { it.toMessageSubject() },
        newAlertCount = newAlertCount,
        newMessageCount = newMessageCount,
        categories = categoryDTOs.map { it.toCategory() },
        managedAccounts = managedAccountDTOs.map { it.toManagedAccount() }
    )
}