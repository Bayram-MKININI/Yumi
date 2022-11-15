package net.noliaware.yumi.commun.util

import androidx.annotation.StringRes
import net.noliaware.yumi.commun.domain.model.AppMessage

sealed interface UIEvent {
    data class ShowAppMessage(val appMessage: AppMessage) : UIEvent
    data class ShowError(
        val errorType: ErrorType? = null,
        @StringRes val errorStrRes: Int
    ) : UIEvent
}
