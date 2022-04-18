package net.noliaware.yumi.feature_login.domain.model

import java.io.Serializable

data class MessageSubject(
    val subjectId: Int,
    val subjectLabel: String
) : Serializable