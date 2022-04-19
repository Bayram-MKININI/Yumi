package net.noliaware.yumi.feature_profile.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.CATEGORY_ID
import net.noliaware.yumi.commun.presentation.BaseViewModel
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_profile.data.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class UsedVouchersListFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ProfileRepository
) : BaseViewModel<List<Voucher>>() {

    init {
        savedStateHandle.get<String>(CATEGORY_ID)?.let { callGetVoucherList(it) }
    }

    private fun callGetVoucherList(selectedCategoryId: String) {
        viewModelScope.launch {
            repository.getUsedVoucherList(selectedCategoryId).onEach { result ->
                handleResponse(result)
            }.launchIn(this)
        }
    }
}