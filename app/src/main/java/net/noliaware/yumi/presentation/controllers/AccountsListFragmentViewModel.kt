package net.noliaware.yumi.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import net.noliaware.yumi.commun.CONNECT_DATA
import net.noliaware.yumi.commun.MANAGED_ACCOUNTS_DATA
import net.noliaware.yumi.domain.model.ConnectData
import net.noliaware.yumi.domain.model.ManagedAccount

class AccountsListFragmentViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val managedAccounts = savedStateHandle.get<List<ManagedAccount>>(MANAGED_ACCOUNTS_DATA)
}