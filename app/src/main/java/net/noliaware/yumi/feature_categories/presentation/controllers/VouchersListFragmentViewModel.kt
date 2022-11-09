package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.CATEGORY_ID
import net.noliaware.yumi.commun.CATEGORY_LABEL
import net.noliaware.yumi.commun.DATA_SHOULD_REFRESH
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.feature_categories.data.repository.CategoryRepository
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import javax.inject.Inject

@HiltViewModel
class VouchersListFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: CategoryRepository
) : ViewModel() {

    val eventsHelper = EventsHelper<List<Voucher>>()
    val categoryLabel get() = savedStateHandle.get<String>(CATEGORY_LABEL).orEmpty()
    var dataShouldRefresh
        get() = savedStateHandle.get<Boolean>(DATA_SHOULD_REFRESH)
        set(value) = savedStateHandle.set(DATA_SHOULD_REFRESH, value)

    init {
        callGetVoucherList()
    }

    fun callGetVoucherList() {
        savedStateHandle.get<String>(CATEGORY_ID)?.let { selectedCategoryId ->
            viewModelScope.launch {
                repository.getVoucherList(selectedCategoryId).onEach { result ->
                    eventsHelper.handleResponse(result)
                }.launchIn(this)
            }
        }
    }
}