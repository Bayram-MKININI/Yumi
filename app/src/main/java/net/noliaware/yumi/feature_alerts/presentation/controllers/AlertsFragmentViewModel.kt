package net.noliaware.yumi.feature_alerts.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi.feature_alerts.data.repository.AlertsRepository
import javax.inject.Inject

@HiltViewModel
class AlertsFragmentViewModel @Inject constructor(
    alertsRepository: AlertsRepository
) : ViewModel() {
    val alerts = alertsRepository.getAlertList().cachedIn(viewModelScope)
}