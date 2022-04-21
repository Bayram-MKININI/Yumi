package net.noliaware.yumi.commun.util

sealed class UIEvent {
    data class ShowSnackBar(val errorType: ErrorType, val errorMessage: String? = null) : UIEvent()
}
