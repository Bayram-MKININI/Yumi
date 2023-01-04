package net.noliaware.yumi.feature_login.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.feature_login.data.repository.LoginRepository
import net.noliaware.yumi.feature_login.domain.model.AccountData
import javax.inject.Inject

@HiltViewModel
class AccountsListFragmentViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    val eventsHelper = EventsHelper<AccountData>()
    fun getManagedProfiles() = loginRepository.getManagedProfileList().cachedIn(viewModelScope)

    fun callSelectAccountForLogin(login: String) {
        viewModelScope.launch {
            loginRepository.selectAccountForId(login).onEach { result ->
                eventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}