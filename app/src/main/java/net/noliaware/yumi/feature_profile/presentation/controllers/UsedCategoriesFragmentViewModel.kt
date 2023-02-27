package net.noliaware.yumi.feature_profile.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_profile.data.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class UsedCategoriesFragmentViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val eventsHelper = EventsHelper<List<Category>>()

    init {
        callGetUsedCategories()
    }

    private fun callGetUsedCategories() {
        viewModelScope.launch {
            profileRepository.getUsedCategories().onEach { result ->
                eventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}