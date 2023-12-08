package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.ApiParameters
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.feature_categories.domain.model.VoucherRequest
import net.noliaware.yumi.feature_categories.domain.repository.CategoryRepository
import javax.inject.Inject

@HiltViewModel
class VoucherOngoingRequestListFragmentViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val voucherId get() = savedStateHandle.get<String>(ApiParameters.VOUCHER_ID)
    val getVoucherRequestsEventsHelper = EventsHelper<List<VoucherRequest>>()
    val deleteVoucherRequestEventsHelper = EventsHelper<Boolean>()

    init {
        callGetVoucherRequestList()
    }

    fun callGetVoucherRequestList() {
        voucherId?.let {
            viewModelScope.launch {
                categoryRepository.getVoucherRequestListById(it).onEach { result ->
                    getVoucherRequestsEventsHelper.handleResponse(result)
                }.launchIn(this)
            }
        }
    }

    fun callRemoveVoucherRequestById(requestId: String) {
        viewModelScope.launch {
            categoryRepository.removeVoucherRequestById(requestId).onEach { result ->
                deleteVoucherRequestEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}