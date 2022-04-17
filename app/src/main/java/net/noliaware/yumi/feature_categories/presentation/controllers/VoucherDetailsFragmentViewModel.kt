package net.noliaware.yumi.feature_categories.presentation.controllers

import android.graphics.Bitmap
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.VOUCHER_ID
import net.noliaware.yumi.commun.util.QRCodeGenerator
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.commun.util.UIEvent
import net.noliaware.yumi.commun.util.ViewModelState
import net.noliaware.yumi.feature_categories.data.repository.CategoryRepository
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import javax.inject.Inject

@HiltViewModel
class VoucherDetailsFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CategoryRepository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ViewModelState<Voucher>())
    val stateFlow = _stateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<String>(VOUCHER_ID)?.let { callGetVoucherById(it) }
    }

    private fun callGetVoucherById(voucherId: String) {

        viewModelScope.launch {

            repository.getVoucherById(voucherId).onEach { result ->

                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _stateFlow.value = ViewModelState(it)
                        }
                    }
                    is Resource.Loading -> {
                        _stateFlow.value = ViewModelState()
                    }
                    is Resource.Error -> {
                        _stateFlow.value = ViewModelState()
                        _eventFlow.emit(UIEvent.ShowSnackBar(result.dataError))
                    }
                }
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