package net.noliaware.yumi.feature_categories.presentation.controllers

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.QR_CODE
import net.noliaware.yumi.commun.QR_CODE_SIZE
import net.noliaware.yumi.commun.util.QRCodeGenerator
import net.noliaware.yumi.commun.util.ViewModelState
import javax.inject.Inject

@HiltViewModel
class QrCodeFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ViewModelState<Bitmap>())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        val qrCode = savedStateHandle.get<String>(QR_CODE) ?: ""
        val qrCodeSize = savedStateHandle.get<Int>(QR_CODE_SIZE) ?: 0
        generateQrCodeForCode(qrCode, qrCodeSize)
    }

    private fun generateQrCodeForCode(code: String, size: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            QRCodeGenerator.encodeAsBitmap(code, size)?.let { bitmap ->
                _stateFlow.value = ViewModelState(bitmap)
            }
        }
    }
}