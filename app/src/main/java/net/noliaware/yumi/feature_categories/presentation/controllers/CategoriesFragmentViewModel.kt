package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.feature_categories.data.repository.CategoryRepository
import net.noliaware.yumi.feature_categories.domain.model.Category
import javax.inject.Inject

@HiltViewModel
class CategoriesFragmentViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val eventsHelper = EventsHelper<List<Category>>()

    init {
        callGetAvailableCategories()
    }

    fun callGetAvailableCategories() {
        viewModelScope.launch {
            categoryRepository.getAvailableCategories().onEach { result ->
                eventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}