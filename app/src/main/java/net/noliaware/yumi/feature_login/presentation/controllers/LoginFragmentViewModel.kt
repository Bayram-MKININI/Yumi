package net.noliaware.yumi.feature_login.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.commun.util.UIEvent
import net.noliaware.yumi.commun.util.ViewModelState
import net.noliaware.yumi.feature_login.data.repository.DataStoreRepository
import net.noliaware.yumi.feature_login.data.repository.LoginRepository
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_login.domain.model.InitData
import net.noliaware.yumi.feature_login.domain.model.UserPreferences
import org.json.JSONArray

class LoginFragmentViewModel @AssistedInject constructor(
    private val repository: LoginRepository,
    @Assisted private val dataStoreRepository: DataStoreRepository
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
            dataStoreRepository.readFromDataStore.onEach { userPreferences ->
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
                handleWSResponse(_initStateFlow, result)
            }.launchIn(this)
        }
    }

    fun callConnectWebserviceWithIndexes(passwordIndexes: List<Int>) {
        viewModelScope.launch {
            repository.getAccountData(JSONArray(passwordIndexes).toString()).onEach { result ->
                handleWSResponse(_connectStateFlow, result)
            }.launchIn(this)
        }
    }

    private suspend fun <T> handleWSResponse(
        stateFlow: MutableStateFlow<ViewModelState<T>>,
        result: Resource<T>
    ) {
        when (result) {
            is Resource.Success -> {
                result.data?.let {
                    stateFlow.value = ViewModelState(it)
                }
            }
            is Resource.Loading -> {
                stateFlow.value = ViewModelState()
            }
            is Resource.Error -> {
                stateFlow.value = ViewModelState()
                _eventFlow.emit(UIEvent.ShowSnackBar(result.errorType, result.errorMessage))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(dataStoreRepository: DataStoreRepository): LoginFragmentViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            dataStoreRepository: DataStoreRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(dataStoreRepository) as T
            }
        }
    }
}