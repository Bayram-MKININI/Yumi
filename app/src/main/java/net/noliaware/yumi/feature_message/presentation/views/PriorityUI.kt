package net.noliaware.yumi.feature_message.presentation.views

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriorityUI(
    val resIcon: Int,
    val label: String
) : Parcelable