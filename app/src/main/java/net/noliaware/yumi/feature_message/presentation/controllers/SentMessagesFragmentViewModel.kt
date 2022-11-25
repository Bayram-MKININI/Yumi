package net.noliaware.yumi.feature_message.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.LIST_PAGE_SIZE
import net.noliaware.yumi.feature_message.data.repository.OutboxMessagePagingSource
import net.noliaware.yumi.feature_message.domain.model.Message
import javax.inject.Inject

@HiltViewModel
class SentMessagesFragmentViewModel @Inject constructor(
    private val pagingSource: OutboxMessagePagingSource
) : ViewModel() {

    val messages: Flow<PagingData<Message>> = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        pagingSource
    }.flow.cachedIn(viewModelScope)
}