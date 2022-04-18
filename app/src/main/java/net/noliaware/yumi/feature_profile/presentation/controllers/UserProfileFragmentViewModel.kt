package net.noliaware.yumi.feature_profile.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.commun.util.UIEvent
import net.noliaware.yumi.commun.util.ViewModelState
import net.noliaware.yumi.feature_profile.data.repository.ProfileRepository
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
import javax.inject.Inject

@HiltViewModel
class UserProfileFragmentViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ViewModelState<UserProfile>())
    val stateFlow = _stateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        callGetUserProfile()
    }

    private fun callGetUserProfile() {

        viewModelScope.launch {

            repository.getUserProfile().onEach { result ->

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