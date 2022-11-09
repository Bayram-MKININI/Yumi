package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.ACCOUNT_DATA
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.feature_alerts.data.repository.AlertsRepository
import net.noliaware.yumi.feature_alerts.domain.model.Alert
import net.noliaware.yumi.feature_categories.data.repository.CategoryRepository
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_message.data.repository.MessageRepository
import net.noliaware.yumi.feature_message.domain.model.Message
import net.noliaware.yumi.feature_profile.data.repository.ProfileRepository
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val profileRepository: ProfileRepository,
    private val messageRepository: MessageRepository,
    private val alertsRepository: AlertsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val accountData get() = savedStateHandle.get<AccountData>(ACCOUNT_DATA)
    val messageSubjects get() = accountData?.messageSubjects

    val availableCategoriesEventsHelper = EventsHelper<List<Category>>()
    val userProfileEventsHelper = EventsHelper<UserProfile>()
    val messageListEventsHelper = EventsHelper<List<Message>>()
    val alertListEventsHelper = EventsHelper<List<Alert>>()

    private val wsCallsFlow = MutableSharedFlow<Job>(extraBufferCapacity = 1)

    init {
        wsCallsFlow.onEach { job ->
            job.join()
        }.launchIn(viewModelScope)
    }

    fun callGetAvailableCategories() {
        viewModelScope.launch {
            categoryRepository.getAvailableCategories().onEach { result ->
                availableCategoriesEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }

    fun callGetUserProfile() {
        wsCallsFlow.tryEmit(
            viewModelScope.launch(start = CoroutineStart.LAZY) {
                userProfileEventsHelper.handleResponse(profileRepository.getUserProfile())
            }
        )
    }

    fun callGetInboxMessageList() {
        wsCallsFlow.tryEmit(
            viewModelScope.launch(start = CoroutineStart.LAZY) {
                messageListEventsHelper.handleResponse(messageRepository.getMessageList())
            }
        )
    }

    fun callGetAlertList() {
        wsCallsFlow.tryEmit(
            viewModelScope.launch(start = CoroutineStart.LAZY) {
                alertListEventsHelper.handleResponse(alertsRepository.getAlertList())
            }
        )
    }
}