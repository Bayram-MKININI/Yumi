package net.noliaware.yumi.feature_message.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.MESSAGE_SUBJECTS_DATA
import net.noliaware.yumi.commun.presentation.BaseViewModel
import net.noliaware.yumi.feature_login.domain.model.MessageSubject
import net.noliaware.yumi.feature_message.data.repository.MessageRepository
import javax.inject.Inject

@HiltViewModel
class SendMailFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MessageRepository
) : BaseViewModel<Boolean>() {

    val messageSubjects get() = savedStateHandle.get<List<MessageSubject>>(MESSAGE_SUBJECTS_DATA)

    fun callSendMessage(messageSubjectId: String, messageBody: String) {
        viewModelScope.launch {
            repository.sendMessage(messageSubjectId, messageBody).onEach { result ->
                handleResponse(result)
            }.launchIn(this)
        }
    }
}