package net.noliaware.yumi.feature_login.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageSubject(
    val subjectId: Int,
    val subjectLabel: String
) : Parcelable