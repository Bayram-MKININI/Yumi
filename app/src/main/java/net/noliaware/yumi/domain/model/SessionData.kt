package net.noliaware.yumi.domain.model

data class SessionData(
    var login: String = "",
    var sessionId: String = "",
    var deviceId: String = "",
    var sessionToken: String = "",
    var encryptionVector: String = ""
)