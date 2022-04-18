package net.noliaware.yumi.feature_message.domain.model

data class Message(
    val messageId: Int,
    val messageType: String,
    val messageReadStatus: Int,
    val messageDate: String,
    val messageTime: Int,
    val messageFrom: String,
    val messageSubject: String,
    val messageBody: String?,
    val messageRank: Int,
    val messageTotal: Int
)