package net.noliaware.yumi.feature_categories.presentation.controllers

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.ApiParameters.VOUCHER_CODE_DATA
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.commun.util.QRCodeGenerator
import net.noliaware.yumi.commun.util.ViewState
import net.noliaware.yumi.commun.util.ViewState.DataState
import net.noliaware.yumi.commun.util.ViewState.LoadingState
import net.noliaware.yumi.feature_categories.domain.model.VoucherCodeData
import net.noliaware.yumi.feature_categories.domain.repository.CategoryRepository
import javax.inject.Inject

@HiltViewModel
class QrCodeFragmentViewModel @Inject constructor(
    private val repository: CategoryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _qrCodeStateFlow: MutableStateFlow<ViewState<Bitmap>> by lazy {
        MutableStateFlow(DataState())
    }
    val qrCodeStateFlow = _qrCodeStateFlow.asStateFlow()

    private val voucherCodeData get() = savedStateHandle.get<VoucherCodeData>(VOUCHER_CODE_DATA)
    val useVoucherEventsHelper = EventsHelper<Boolean>()

    init {
        voucherCodeData?.let { voucherCodeData ->
            voucherCodeData.voucherCode?.let { voucherCodeStr ->
                generateQrCodeForCode(
                    voucherCodeStr,
                    voucherCodeData.voucherCodeSize
                )
            }
        }
    }

    private fun generateQrCodeForCode(code: String, size: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _qrCodeStateFlow.value = LoadingState()
            QRCodeGenerator.encodeAsBitmap(code, size)?.let { bitmap ->
                _qrCodeStateFlow.value = DataState(bitmap)
            }
        }
    }

    fun callUseVoucher() {
        voucherCodeData?.voucherId?.let { voucherId ->
            viewModelScope.launch {
                repository.useVoucherById(voucherId).onEach { result ->
                    useVoucherEventsHelper.handleResponse(result)
                }.launchIn(this)
            }
        }
    }
}