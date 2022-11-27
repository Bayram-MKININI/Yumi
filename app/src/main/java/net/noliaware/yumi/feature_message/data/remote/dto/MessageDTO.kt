package net.noliaware.yumi.feature_message.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_message.domain.model.Message

@JsonClass(generateAdapter = true)
data class MessageDTO(
    @Json(name = "messageId")
    val messageId: String,
    @Json(name = "messageDate")
    val messageDate: String,
    @Json(name = "messageTime")
    val messageTime: String,
    @Json(name = "messageSubject")
    val messageSubject: String,
    @Json(name = "messagePreview")
    val messagePreview: String?,
    @Json(name = "messageType")
    val messageType: String?,
    @Json(name = "messageReadStatus")
    val messageReadStatus: Int?,
    @Json(name = "messageBody")
    val messageBody: String?,
    @Json(name = "messageRank")
    val messageRank: Int?,
    @Json(name = "messageCount")
    val messageCount: Int?
) {
    fun toMessage() = Message(
        messageId = messageId,
        messageDate = messageDate,
        messageTime = messageTime,
        messageSubject = messageSubject,
        messagePreview = messagePreview,
        messageType = messageType,
        messageReadStatus = messageReadStatus,
        messageBody = messageBody
    )
}