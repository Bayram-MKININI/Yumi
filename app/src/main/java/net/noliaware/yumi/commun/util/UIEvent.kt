package net.noliaware.yumi.commun.util

sealed class UIEvent {
    data class ShowSnackBar(val dataError: DataError, val errorMessage: String? = null) : UIEvent()
}
