package net.noliaware.yumi.feature_categories.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.CATEGORY_ID
import net.noliaware.yumi.commun.CATEGORY_LABEL
import net.noliaware.yumi.commun.DATA_SHOULD_REFRESH
import net.noliaware.yumi.commun.LIST_PAGE_SIZE
import net.noliaware.yumi.feature_categories.data.repository.VoucherPagingSource
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import javax.inject.Inject

@HiltViewModel
class VouchersListFragmentViewModel @Inject constructor(
    private val voucherPagingSource: VoucherPagingSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val selectedCategoryId get() = savedStateHandle.get<String>(CATEGORY_ID).orEmpty()
    val categoryLabel get() = savedStateHandle.get<String>(CATEGORY_LABEL).orEmpty()
    var dataShouldRefresh
        get() = savedStateHandle.get<Boolean>(DATA_SHOULD_REFRESH)
        set(value) = savedStateHandle.set(DATA_SHOULD_REFRESH, value)

    val vouchers: Flow<PagingData<Voucher>> = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        voucherPagingSource.apply {
            this.categoryId = selectedCategoryId
        }
    }.flow.cachedIn(viewModelScope)

    fun invalidateDataSource() = voucherPagingSource.invalidate()
}