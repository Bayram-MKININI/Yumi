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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val getVoucherRequestsEventsHelper = EventsHelper<List<VoucherRequest>>()

    init {
        savedStateHandle.get<String>(ApiParameters.VOUCHER_ID)?.let {
            callGetVoucherById(it)
        }
    }

    private fun callGetVoucherById(voucherId: String) {
        viewModelScope.launch {
            categoryRepository.getVoucherRequestListById(voucherId).onEach { result ->
                getVoucherRequestsEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}