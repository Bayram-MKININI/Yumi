package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi.commun.Args.DATA_SHOULD_REFRESH
import net.noliaware.yumi.commun.Args.SELECTED_CATEGORY
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.domain.repository.CategoryRepository
import javax.inject.Inject

@HiltViewModel
class AvailableVouchersListFragmentViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val selectedCategory get() = savedStateHandle.get<Category>(SELECTED_CATEGORY)

    var dataShouldRefresh
        get() = savedStateHandle.getStateFlow(key = DATA_SHOULD_REFRESH, initialValue = false).value
        set(value) {
            savedStateHandle[DATA_SHOULD_REFRESH] = value
        }

    fun getVouchers() = selectedCategory?.categoryId?.let { categoryId ->
        categoryRepository.getAvailableVoucherList(categoryId).cachedIn(viewModelScope)
    }
}