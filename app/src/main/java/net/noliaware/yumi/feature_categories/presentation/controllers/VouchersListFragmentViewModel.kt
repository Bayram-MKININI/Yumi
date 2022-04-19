package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.CATEGORY_ID
import net.noliaware.yumi.commun.presentation.BaseViewModel
import net.noliaware.yumi.feature_categories.data.repository.CategoryRepository
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import javax.inject.Inject

@HiltViewModel
class VouchersListFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CategoryRepository
) : BaseViewModel<List<Voucher>>() {

    init {
        savedStateHandle.get<String>(CATEGORY_ID)?.let { callGetVoucherList(it) }
    }

    private fun callGetVoucherList(selectedCategoryId: String) {
        viewModelScope.launch {
            repository.getVoucherList(selectedCategoryId).onEach { result ->
                handleResponse(result)
            }.launchIn(this)
        }
    }
}