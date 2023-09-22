package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi.commun.Args.SELECTED_CATEGORY
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.domain.repository.CategoryRepository
import javax.inject.Inject

@HiltViewModel
class CancelledVouchersListFragmentViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val selectedCategory get() = savedStateHandle.get<Category>(SELECTED_CATEGORY)

    fun getVouchers() = selectedCategory?.categoryId?.let {
        categoryRepository.getCancelledVoucherList(it).cachedIn(viewModelScope)
    }
}