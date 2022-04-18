package net.noliaware.yumi.feature_message.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_message.domain.model.Message

@JsonClass(generateAdapter = true)
data class MessageDTO(
    @Json(name = "messageId")
    val messageId: Int,
    @Json(name = "messageType")
    val messageType: String,
    @Json(name = "messageReadStatus")
    val messageReadStatus: Int,
    @Json(name = "messageDate")
    val messageDate: String,
    @Json(name = "messageTime")
    val messageTime: Int,
    @Json(name = "messageFrom")
    val messageFrom: String,
    @Json(name = "messageSubject")
    val messageSubject: String,
    @Json(name = "messageBody")
    val messageBody: String?,
    @Json(name = "messageRank")
    val messageRank: Int,
    @Json(name = "messageTotal")
    val messageTotal: Int
) {
    fun toMessage() = Message(
        messageId = messageId,
        messageType = messageType,
        messageReadStatus = messageReadStatus,
        messageDate = messageDate,
        messageTime = messageTime,
        messageFrom = messageFrom,
        messageSubject = messageSubject,
        messageBody = messageBody,
        messageRank = messageRank,
        messageTotal = messageTotal
    )
}