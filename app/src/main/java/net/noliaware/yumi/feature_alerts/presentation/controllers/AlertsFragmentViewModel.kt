package net.noliaware.yumi.feature_alerts.presentation.controllers

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.presentation.BaseViewModel
import net.noliaware.yumi.feature_alerts.data.repository.AlertsRepository
import net.noliaware.yumi.feature_alerts.domain.model.Alert
import javax.inject.Inject

@HiltViewModel
class AlertsFragmentViewModel @Inject constructor(
    private val repository: AlertsRepository
) : BaseViewModel<List<Alert>>() {

    init {
        callGetAlertList()
    }

    private fun callGetAlertList() {
        viewModelScope.launch {
            repository.getAlertList().onEach { result ->
                handleResponse(result)
            }.launchIn(this)
        }
    }
}