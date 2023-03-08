package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.ACCOUNT_DATA
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.feature_categories.data.repository.CategoryRepository
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_login.domain.model.AccountData
import javax.inject.Inject

@HiltViewModel
class CategoriesFragmentViewModel @Inject constructor(
    private val repository: CategoryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val accountData get() = savedStateHandle.get<AccountData>(ACCOUNT_DATA)
    val availableCategoriesEventsHelper = EventsHelper<List<Category>>()
    val cancelledCategoriesEventsHelper = EventsHelper<List<Category>>()
    val usedCategoriesEventsHelper = EventsHelper<List<Category>>()

    private val _badgeCountFlow: MutableStateFlow<Int> = MutableStateFlow(
        accountData?.availableVoucherCount ?: 0
    )
    val badgeCountFlow = _badgeCountFlow.asStateFlow()

    private val _onDataRefreshedEventFlow = MutableSharedFlow<Unit>()
    val onDataRefreshedEventFlow = _onDataRefreshedEventFlow.asSharedFlow()

    fun callGetAvailableCategories() {
        viewModelScope.launch {
            repository.getAvailableCategories().onEach { result ->
                availableCategoriesEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }

    fun callGetCancelledCategories() {
        viewModelScope.launch {
            repository.getCancelledCategories().onEach { result ->
                cancelledCategoriesEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }

    fun callGetUsedCategories() {
        viewModelScope.launch {
            repository.getUsedCategories().onEach { result ->
                usedCategoriesEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }

    fun setBadgeCountValue(count: Int) {
        _badgeCountFlow.value = count
    }

    fun sendDataRefreshedEvent() {
        viewModelScope.launch {
            _onDataRefreshedEventFlow.emit(Unit)
        }
    }
}