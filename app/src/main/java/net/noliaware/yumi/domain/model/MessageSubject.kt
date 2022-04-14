package net.noliaware.yumi.domain.model

import java.io.Serializable

data class MessageSubject(
    val subjectId: Int,
    val subjectLabel: String
) : Serializable