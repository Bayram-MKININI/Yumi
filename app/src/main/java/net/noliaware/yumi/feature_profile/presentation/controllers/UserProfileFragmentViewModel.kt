package net.noliaware.yumi.feature_profile.presentation.controllers

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.presentation.BaseViewModel
import net.noliaware.yumi.feature_profile.data.repository.ProfileRepository
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
import javax.inject.Inject

@HiltViewModel
class UserProfileFragmentViewModel @Inject constructor(
    private val repository: ProfileRepository
) : BaseViewModel<UserProfile>() {

    init {
        callGetUserProfile()
    }

    private fun callGetUserProfile() {
        viewModelScope.launch {
            repository.getUserProfile().onEach { result ->
                handleResponse(result)
            }.launchIn(this)
        }
    }
}