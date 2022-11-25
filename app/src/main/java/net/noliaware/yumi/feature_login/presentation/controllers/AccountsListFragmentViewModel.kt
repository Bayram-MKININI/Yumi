package net.noliaware.yumi.feature_login.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.LIST_PAGE_SIZE
import net.noliaware.yumi.commun.presentation.EventsHelper
import net.noliaware.yumi.feature_login.data.repository.LoginRepository
import net.noliaware.yumi.feature_login.data.repository.ManagedAccountPagingSource
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
import javax.inject.Inject

@HiltViewModel
class AccountsListFragmentViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val pagingSource: ManagedAccountPagingSource
) : ViewModel() {

    val eventsHelper = EventsHelper<AccountData>()

    val managedProfiles: Flow<PagingData<UserProfile>> = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        pagingSource
    }.flow.cachedIn(viewModelScope)

    fun callSelectAccountForLogin(login: String) {
        viewModelScope.launch {
            repository.selectAccountForId(login).onEach { result ->
                eventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}