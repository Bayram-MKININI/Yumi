package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi.commun.Args.CATEGORY
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.domain.repository.CategoryRepository
import javax.inject.Inject

@HiltViewModel
class UsedVouchersListFragmentViewModel @Inject constructor(
    private val profileRepository: CategoryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val selectedCategory get() = savedStateHandle.get<Category>(CATEGORY)

    fun getVouchers() = selectedCategory?.categoryId?.let {
        profileRepository.getUsedVoucherList(it).cachedIn(viewModelScope)
    }
}