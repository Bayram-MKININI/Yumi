package net.noliaware.yumi.feature_message.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.MESSAGE_ID
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.feature_message.data.repository.MessageRepository
import net.noliaware.yumi.feature_message.domain.model.Message
import javax.inject.Inject

@HiltViewModel
class ReadInboxMailFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MessageRepository,
) : ViewModel() {

    val eventsHelper = EventsHelper<Message>()

    init {
        savedStateHandle.get<String>(MESSAGE_ID)?.let {
            callGetMessageForId(it)
        }
    }

    private fun callGetMessageForId(messageId: String) {
        viewModelScope.launch {
            repository.getInboxMessageForId(messageId).onEach { result ->
                eventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}