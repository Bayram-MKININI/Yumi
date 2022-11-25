package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.CATEGORIES_DATA
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.feature_categories.data.repository.CategoryRepository
import net.noliaware.yumi.feature_categories.domain.model.Category
import javax.inject.Inject

@HiltViewModel
class CategoriesFragmentViewModel @Inject constructor(
    private val repository: CategoryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val categories get() = savedStateHandle.get<List<Category>>(CATEGORIES_DATA).orEmpty()
    val availableCategoriesEventsHelper = EventsHelper<List<Category>>()

    fun callGetAvailableCategories() {
        viewModelScope.launch {
            repository.getAvailableCategories().onEach { result ->
                availableCategoriesEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}