package net.noliaware.yumi.feature_categories.presentation.controllers

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.VOUCHER_CODE_DATA
import net.noliaware.yumi.commun.util.QRCodeGenerator
import net.noliaware.yumi.commun.util.ViewModelState
import net.noliaware.yumi.feature_categories.domain.model.VoucherCodeData
import javax.inject.Inject

@HiltViewModel
class QrCodeFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ViewModelState<Bitmap>())
    val stateFlow = _stateFlow.asStateFlow()
    val voucherCodeData get() = savedStateHandle.get<VoucherCodeData>(VOUCHER_CODE_DATA)

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
            QRCodeGenerator.encodeAsBitmap(code, size)?.let { bitmap ->
                _stateFlow.value = ViewModelState(bitmap)
            }
        }
    }
}