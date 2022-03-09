package net.noliaware.yumi.controllers

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.noliaware.yumi.utils.QRCodeGenerator

class VoucherDetailsFragmentViewModel(application: Application) : AndroidViewModel(application) {

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