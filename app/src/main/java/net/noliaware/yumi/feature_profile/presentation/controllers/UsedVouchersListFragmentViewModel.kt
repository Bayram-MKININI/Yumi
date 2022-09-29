package net.noliaware.yumi.feature_profile.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.CATEGORY_ID
import net.noliaware.yumi.commun.CATEGORY_LABEL
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_profile.data.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class UsedVouchersListFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: ProfileRepository
) : ViewModel() {

    val eventsHelper = EventsHelper<List<Voucher>>()
    val categoryLabel get() = savedStateHandle.get<String>(CATEGORY_LABEL) ?: ""

    init {
        savedStateHandle.get<String>(CATEGORY_ID)?.let { callGetVoucherList(it) }
    }

    private fun callGetVoucherList(selectedCategoryId: String) {
        viewModelScope.launch {
            repository.getUsedVoucherList(selectedCategoryId).onEach { result ->
                eventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}