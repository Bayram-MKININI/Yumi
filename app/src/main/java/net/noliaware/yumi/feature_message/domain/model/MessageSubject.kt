package net.noliaware.yumi.feature_message.domain.model

import java.io.Serializable

data class MessageSubject(
    val subjectId: Int,
    val subjectLabel: String
) : Serializable