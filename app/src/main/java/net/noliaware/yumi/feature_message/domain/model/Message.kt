package net.noliaware.yumi.feature_message.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import net.noliaware.yumi.commun.domain.model.Priority

@Parcelize
data class Message(
    val messageId: String,
    val messageDate: String,
    val messageTime: String,
    val messageType: String?,
    val messagePriority: Priority?,
    val messageSubject: String,
    val messagePreview: String?,
    val isMessageRead: Boolean,
    val messageBody: String?
) : Parcelable