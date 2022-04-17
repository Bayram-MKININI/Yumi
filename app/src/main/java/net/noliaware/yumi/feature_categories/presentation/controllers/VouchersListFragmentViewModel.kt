package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.CATEGORY_ID
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.commun.util.UIEvent
import net.noliaware.yumi.commun.util.ViewModelState
import net.noliaware.yumi.feature_categories.data.repository.CategoryRepository
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import javax.inject.Inject

@HiltViewModel
class VouchersListFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CategoryRepository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ViewModelState<List<Voucher>>())
    val stateFlow = _stateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<String>(CATEGORY_ID)?.let { callGetVoucherList(it) }
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