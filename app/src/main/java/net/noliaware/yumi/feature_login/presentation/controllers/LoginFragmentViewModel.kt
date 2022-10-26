package net.noliaware.yumi.feature_login.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.util.UIEvent
import net.noliaware.yumi.commun.util.ViewModelState
import net.noliaware.yumi.commun.util.handleWSResponse
import net.noliaware.yumi.feature_login.data.repository.DataStoreRepository
import net.noliaware.yumi.feature_login.data.repository.LoginRepository
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_login.domain.model.InitData
import net.noliaware.yumi.feature_login.domain.model.UserPreferences
import org.json.JSONArray
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _prefsStateFlow = MutableStateFlow(ViewModelState<UserPreferences>())
    val prefsStateFlow = _prefsStateFlow.asStateFlow()

    private val _initStateFlow = MutableStateFlow(ViewModelState<InitData>())
    val initStateFlow = _initStateFlow.asStateFlow()

    private val _connectStateFlow = MutableStateFlow(ViewModelState<AccountData>())
    val connectStateFlow = _connectStateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        callReadPreferences()
    }

    private fun callReadPreferences() {
        viewModelScope.launch {
            dataStoreRepository.readUserPreferences().onEach { userPreferences ->
                _prefsStateFlow.value = ViewModelState(userPreferences)
            }.launchIn(this)
        }
    }

    fun saveLoginPreferences(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveLogin(login)
        }
    }

    fun saveDeviceIdPreferences(deviceId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveDeviceId(deviceId)
        }
    }

    fun clearUserPreferences() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.clearDataStore()
        }
    }

    fun callInitWebservice(androidId: String, deviceId: String?, login: String) {
        viewModelScope.launch {
            repository.getInitData(androidId, deviceId, login).onEach { result ->
                handleWSResponse(result, _initStateFlow, _eventFlow)
            }.launchIn(this)
        }
    }

    fun callConnectWebserviceWithIndexes(passwordIndexes: List<Int>) {
        viewModelScope.launch {
            repository.getAccountData(JSONArray(passwordIndexes).toString()).onEach { result ->
                handleWSResponse(result, _connectStateFlow, _eventFlow)
            }.launchIn(this)
        }
    }
}