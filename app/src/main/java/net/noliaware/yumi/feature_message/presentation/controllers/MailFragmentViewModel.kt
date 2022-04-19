package net.noliaware.yumi.feature_message.presentation.controllers

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.presentation.BaseViewModel
import net.noliaware.yumi.feature_message.data.repository.MessageRepository
import net.noliaware.yumi.feature_message.domain.model.Message
import javax.inject.Inject

@HiltViewModel
class MailFragmentViewModel @Inject constructor(
    private val repository: MessageRepository
) : BaseViewModel<List<Message>>() {

    init {
        callGetInboxMessageList()
    }

    private fun callGetInboxMessageList() {
        viewModelScope.launch {
            repository.getMessageList().onEach { result ->
                handleResponse(result)
            }.launchIn(this)
        }
    }
}