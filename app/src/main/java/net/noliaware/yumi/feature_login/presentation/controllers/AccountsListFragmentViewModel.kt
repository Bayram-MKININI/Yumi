package net.noliaware.yumi.feature_login.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.MANAGED_PROFILES_DATA
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.feature_login.data.repository.LoginRepository
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
import javax.inject.Inject

@HiltViewModel
class AccountsListFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: LoginRepository
) : ViewModel() {

    val managedProfiles get() = savedStateHandle.get<List<UserProfile>>(MANAGED_PROFILES_DATA)
    val eventsHelper = EventsHelper<AccountData>()

    fun callSelectAccountForLogin(login: String) {
        viewModelScope.launch {
            repository.selectAccountForId(login).onEach { result ->
                eventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}