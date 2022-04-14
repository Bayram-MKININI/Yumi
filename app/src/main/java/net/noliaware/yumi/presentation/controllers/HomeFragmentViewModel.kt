package net.noliaware.yumi.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import net.noliaware.yumi.commun.CONNECT_DATA
import net.noliaware.yumi.domain.model.ConnectData

class HomeFragmentViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val connectData = savedStateHandle.get<ConnectData>(CONNECT_DATA)
}