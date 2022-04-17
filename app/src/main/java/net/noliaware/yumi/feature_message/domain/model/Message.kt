package net.noliaware.yumi.feature_message.domain.model

data class Message(
    var id: String = "",
    var from: String = "",
    var to: String = "",
    var timestamp: Long = 0,
    var subject: String = "",
    var text: String = ""
)