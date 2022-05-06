package net.noliaware.yumi.feature_message.domain.model

enum class MessageOrigin {
    INBOX, OUTBOX
}

data class Message(
    val messageId: String,
    val messageDate: String,
    val messageTime: String,
    val messageSubject: String,
    val messageRank: Int?,
    val messageTotal: Int?,
    val messageToRecipients: String?,
    val messageType: String?,
    val messageReadStatus: Int?,
    val messageFrom: String?,
    val messageBody: String?,
    var messageOrigin: MessageOrigin? = null
)