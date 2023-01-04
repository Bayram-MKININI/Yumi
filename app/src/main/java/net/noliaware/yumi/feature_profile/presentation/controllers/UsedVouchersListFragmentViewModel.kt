package net.noliaware.yumi.feature_profile.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi.commun.CATEGORY_ID
import net.noliaware.yumi.commun.CATEGORY_LABEL
import net.noliaware.yumi.feature_profile.data.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class UsedVouchersListFragmentViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val selectedCategoryId get() = savedStateHandle.get<String>(CATEGORY_ID).orEmpty()
    val categoryLabel get() = savedStateHandle.get<String>(CATEGORY_LABEL).orEmpty()

    fun getVouchers() = profileRepository.getUsedVoucherList(selectedCategoryId).cachedIn(viewModelScope)
}