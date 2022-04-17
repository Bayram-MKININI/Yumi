package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi.commun.CATEGORIES_DATA
import net.noliaware.yumi.feature_categories.domain.model.Category
import javax.inject.Inject

@HiltViewModel
class CategoriesFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val categories get() = savedStateHandle.get<List<Category>>(CATEGORIES_DATA)
}