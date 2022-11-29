package net.noliaware.yumi.feature_message.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi.feature_message.data.repository.MessageRepository
import javax.inject.Inject

@HiltViewModel
class ReceivedMessagesFragmentViewModel @Inject constructor(
    messageRepository: MessageRepository
) : ViewModel() {
    val messages = messageRepository.getReceivedMessageList().cachedIn(viewModelScope)
}