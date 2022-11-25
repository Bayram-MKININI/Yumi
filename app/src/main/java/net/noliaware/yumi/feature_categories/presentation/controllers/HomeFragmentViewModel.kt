package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi.commun.ACCOUNT_DATA
import net.noliaware.yumi.feature_login.domain.model.AccountData
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val accountData get() = savedStateHandle.get<AccountData>(ACCOUNT_DATA)
}