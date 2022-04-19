package net.noliaware.yumi.feature_message.domain.model

data class Message(
    val messageId: String,
    val messageType: String,
    val messageReadStatus: Int,
    val messageDate: String,
    val messageTime: String,
    val messageFrom: String,
    val messageSubject: String,
    val messageBody: String?,
    val messageRank: Int,
    val messageTotal: Int
)