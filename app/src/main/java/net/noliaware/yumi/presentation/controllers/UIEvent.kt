package net.noliaware.yumi.presentation.controllers

import net.noliaware.yumi.commun.util.DataError

sealed class UIEvent {
    data class ShowSnackBar(val dataError: DataError) : UIEvent()
}
