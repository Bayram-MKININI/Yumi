package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.VOUCHER_ID
import net.noliaware.yumi.commun.util.UIEvent
import net.noliaware.yumi.commun.util.ViewModelState
import net.noliaware.yumi.commun.util.handleWSResponse
import net.noliaware.yumi.feature_categories.data.repository.CategoryRepository
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.domain.model.VoucherStatus
import javax.inject.Inject

@HiltViewModel
class VoucherDetailsFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CategoryRepository
) : ViewModel() {

    private val _getVoucherStateFlow = MutableStateFlow(ViewModelState<Voucher>())
    val getVoucherStateFlow = _getVoucherStateFlow.asStateFlow()

    private val _getVoucherStatusStateFlow = MutableStateFlow(ViewModelState<VoucherStatus>())
    val getVoucherStatusStateFlow = _getVoucherStatusStateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<String>(VOUCHER_ID)?.let {
            callGetVoucherById(it)
        }
    }

    private fun callGetVoucherById(voucherId: String) {
        viewModelScope.launch {
            repository.getVoucherById(voucherId).onEach { result ->
                handleWSResponse(result, _getVoucherStateFlow, _eventFlow)
            }.launchIn(this)
        }
    }

    fun callGetVoucherStatusById(voucherId: String) {
        viewModelScope.launch {
            repository.getVoucherStatusById(voucherId).onEach { result ->
                handleWSResponse(result, _getVoucherStatusStateFlow, _eventFlow)
            }.launchIn(this)
        }
    }
}