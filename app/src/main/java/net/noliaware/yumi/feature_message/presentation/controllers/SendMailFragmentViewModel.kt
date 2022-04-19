package net.noliaware.yumi.feature_message.presentation.controllers

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.presentation.BaseViewModel
import net.noliaware.yumi.feature_message.data.repository.MessageRepository
import javax.inject.Inject

@HiltViewModel
class SendMailFragmentViewModel @Inject constructor(
    private val repository: MessageRepository
) : BaseViewModel<Boolean>() {

    fun callSendMessage(messageSubject: String, messageBody: String) {
        viewModelScope.launch {
            repository.sendMessage(messageSubject, messageBody).onEach { result ->
                handleResponse(result)
            }.launchIn(this)
        }
    }
}