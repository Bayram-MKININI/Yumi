package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.VOUCHER_ID
import net.noliaware.yumi.commun.VOUCHER_VALIDATED
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.feature_categories.data.repository.CategoryRepository
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.domain.model.VoucherStatus
import javax.inject.Inject

@HiltViewModel
class VoucherDetailsFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: CategoryRepository
) : ViewModel() {

    val voucherValidated get() = savedStateHandle.get<Boolean>(VOUCHER_VALIDATED)

    val getVoucherEventsHelper = EventsHelper<Voucher>()
    val getVoucherStatusEventsHelper = EventsHelper<VoucherStatus>()

    init {
        savedStateHandle.get<String>(VOUCHER_ID)?.let {
            callGetVoucherById(it)
        }
    }

    private fun callGetVoucherById(voucherId: String) {
        viewModelScope.launch {
            repository.getVoucherById(voucherId).onEach { result ->
                getVoucherEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }

    fun callGetVoucherStatusById(voucherId: String) {
        viewModelScope.launch {
            repository.getVoucherStatusById(voucherId).onEach { result ->
                getVoucherStatusEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}