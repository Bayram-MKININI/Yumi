package net.noliaware.yumi.feature_alerts.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.LIST_PAGE_SIZE
import net.noliaware.yumi.feature_alerts.data.repository.AlertPagingSource
import net.noliaware.yumi.feature_alerts.domain.model.Alert
import javax.inject.Inject

@HiltViewModel
class AlertsFragmentViewModel @Inject constructor(
    private val pagingSource: AlertPagingSource
) : ViewModel() {

    val alerts: Flow<PagingData<Alert>> = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        pagingSource
    }.flow.cachedIn(viewModelScope)
}