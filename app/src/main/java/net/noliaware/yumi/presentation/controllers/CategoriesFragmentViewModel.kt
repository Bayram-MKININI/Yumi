package net.noliaware.yumi.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import net.noliaware.yumi.commun.CATEGORIES_DATA
import net.noliaware.yumi.domain.model.Category

class CategoriesFragmentViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val categories = savedStateHandle.get<List<Category>>(CATEGORIES_DATA)
}