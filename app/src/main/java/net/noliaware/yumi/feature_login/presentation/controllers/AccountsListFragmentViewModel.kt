package net.noliaware.yumi.feature_login.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.MANAGED_PROFILES_DATA
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.commun.util.UIEvent
import net.noliaware.yumi.commun.util.ViewModelState
import net.noliaware.yumi.feature_login.data.repository.LoginRepository
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_profile.domain.model.AccountProfile
import javax.inject.Inject

@HiltViewModel
class AccountsListFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: LoginRepository
) : ViewModel() {

    val managedProfiles get() = savedStateHandle.get<List<AccountProfile>>(MANAGED_PROFILES_DATA)

    private val _stateFlow = MutableStateFlow(ViewModelState<AccountData>())
    val stateFlow = _stateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun callSelectAccountForLogin(login: String) {

        viewModelScope.launch {

            repository.selectAccountForId(login).onEach { result ->

                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _stateFlow.value = ViewModelState(it)
                        }
                    }
                    is Resource.Loading -> {
                        _stateFlow.value = ViewModelState()
                    }
                    is Resource.Error -> {
                        _stateFlow.value = ViewModelState()
                        _eventFlow.emit(UIEvent.ShowSnackBar(result.dataError))
                    }
                }
            }.launchIn(this)
        }
    }
}