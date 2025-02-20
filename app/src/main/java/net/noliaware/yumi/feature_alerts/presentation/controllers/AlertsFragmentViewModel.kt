package net.noliaware.yumi.feature_alerts.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi.feature_alerts.domain.repository.AlertsRepository
import javax.inject.Inject

@HiltViewModel
class AlertsFragmentViewModel @Inject constructor(
    private val alertsRepository: AlertsRepository
) : ViewModel() {
    fun getAlerts() = alertsRepository.getAlertList().cachedIn(viewModelScope)
}