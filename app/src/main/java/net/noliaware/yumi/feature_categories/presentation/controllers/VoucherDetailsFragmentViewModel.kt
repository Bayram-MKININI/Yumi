package net.noliaware.yumi.feature_categories.presentation.controllers

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.VOUCHER_ID
import net.noliaware.yumi.commun.presentation.BaseViewModel
import net.noliaware.yumi.commun.util.QRCodeGenerator
import net.noliaware.yumi.feature_categories.data.repository.CategoryRepository
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import javax.inject.Inject

@HiltViewModel
class VoucherDetailsFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CategoryRepository
) : BaseViewModel<Voucher>() {

    init {
        savedStateHandle.get<String>(VOUCHER_ID)?.let { callGetVoucherById(it) }
    }

    private fun callGetVoucherById(voucherId: String) {
        viewModelScope.launch {
            repository.getVoucherById(voucherId).onEach { result ->
                handleResponse(result)
            }.launchIn(this)
        }
    }

    private val qrCodeGenerator = QRCodeGenerator()
    val generatedBitmapLiveData = MutableLiveData<Bitmap>()

    fun generateQrCode(content: String, size: Int) {

        viewModelScope.launch {
            qrCodeGenerator.encodeAsBitmap(content, size, size)?.let { bitmap ->
                generatedBitmapLiveData.postValue(bitmap)
            }
        }
    }
}