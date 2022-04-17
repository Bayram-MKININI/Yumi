package net.noliaware.yumi.feature_login.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.commun.util.UIEvent
import net.noliaware.yumi.feature_login.data.repository.LoginRepository
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_login.domain.model.InitData
import net.noliaware.yumi.commun.util.ViewModelState
import org.json.JSONArray
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(private val repository: LoginRepository) :
    ViewModel() {

    private val _initStateFlow = MutableStateFlow(ViewModelState<InitData>())
    val initStateFlow = _initStateFlow.asStateFlow()

    private val _connectStateFlow = MutableStateFlow(ViewModelState<AccountData>())
    val connectStateFlow = _connectStateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun callInitWebservice(androidId: String, deviceId: String?, login: String) {
        viewModelScope.launch {

            repository.getInitData(androidId, deviceId, login).onEach { result ->

                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _initStateFlow.value = ViewModelState(it)
                        }
                    }
                    is Resource.Loading -> {
                        _initStateFlow.value = ViewModelState()
                    }
                    is Resource.Error -> {
                        _initStateFlow.value = ViewModelState()
                        _eventFlow.emit(UIEvent.ShowSnackBar(result.dataError))
                    }
                }
            }.launchIn(this)
        }
    }

    fun callConnectWebserviceWithIndexes(passwordIndexes: List<Int>) {

        viewModelScope.launch {

            repository.getAccountData(JSONArray(passwordIndexes).toString()).onEach { result ->

                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _connectStateFlow.value = ViewModelState(it)
                        }
                    }
                    is Resource.Loading -> {
                        _connectStateFlow.value = ViewModelState()
                    }
                    is Resource.Error -> {
                        _connectStateFlow.value = ViewModelState()
                        _eventFlow.emit(UIEvent.ShowSnackBar(result.dataError))
                    }
                }
            }.launchIn(this)
        }
    }
}