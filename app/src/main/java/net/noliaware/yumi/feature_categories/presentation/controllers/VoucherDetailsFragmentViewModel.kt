package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.ApiParameters.VOUCHER_ID
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.domain.model.VoucherStateData
import net.noliaware.yumi.feature_categories.domain.repository.CategoryRepository
import javax.inject.Inject

@HiltViewModel
class VoucherDetailsFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CategoryRepository
) : ViewModel() {

    val getVoucherEventsHelper = EventsHelper<Voucher>()
    val requestSentEventsHelper = EventsHelper<Boolean>()
    val getVoucherStateDataEventsHelper = EventsHelper<VoucherStateData>()

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

    fun callSendVoucherRequestWithId(
        voucherId: String,
        voucherRequestTypeId: Int,
        voucherRequestComment: String
    ) {
        viewModelScope.launch {
            repository.sendVoucherRequestWithId(
                voucherId,
                voucherRequestTypeId,
                voucherRequestComment
            ).onEach { result ->
                requestSentEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }

    fun callGetVoucherStatusById(voucherId: String) {
        viewModelScope.launch {
            repository.getVoucherStateDataById(voucherId).onEach { result ->
                getVoucherStateDataEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}