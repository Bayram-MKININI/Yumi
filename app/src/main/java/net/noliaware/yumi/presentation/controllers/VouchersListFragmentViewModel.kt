package net.noliaware.yumi.presentation.controllers

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.CATEGORY_ID
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.data.repository.Repository
import net.noliaware.yumi.domain.model.Voucher

class VouchersListFragmentViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ViewModelState<List<Voucher>>())
    val stateFlow = _stateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        val selectedCategoryId = savedStateHandle.get<String>(CATEGORY_ID)
        Log.e("selectedCategoryId", selectedCategoryId.toString())
        selectedCategoryId?.let { }
        callGetVoucherList("s_61e291ce9936a1")
    }

    private fun callGetVoucherList(selectedCategoryId: String) {

        viewModelScope.launch {

            repository.getVoucherList(selectedCategoryId).onEach { result ->

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
}