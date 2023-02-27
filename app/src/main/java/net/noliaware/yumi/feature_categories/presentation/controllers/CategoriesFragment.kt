package net.noliaware.yumi.feature_categories.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.ACCOUNT_DATA
import net.noliaware.yumi.commun.VOUCHERS_LIST_FRAGMENT_TAG
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesView
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesView.CategoriesViewAdapter
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesView.CategoriesViewCallback
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView.CategoryItemViewAdapter
import net.noliaware.yumi.feature_login.domain.model.AccountData

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    companion object {
        fun newInstance(
            accountData: AccountData?
        ) = CategoriesFragment().withArgs(ACCOUNT_DATA to accountData)
    }

    private var categoriesView: CategoriesView? = null
    private val viewModel by viewModels<CategoriesFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.categories_layout, false)?.apply {
            categoriesView = this as CategoriesView
            categoriesView?.callback = categoriesViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
        bindViewToData()
        viewModel.accountData?.let {
            categoriesView?.setUserData(
                it.helloMessage,
                it.userName,
                it.availableVoucherCountSinceLast.formatNumber()
            )
        }
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreenFromSharedEvent(sharedEvent)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.stateFlow.collect { vmState ->
                when (vmState) {
                    is ViewModelState.LoadingState -> Unit
                    is ViewModelState.DataState -> vmState.data?.let { categoryList ->
                        categoriesView?.refreshCategoryList(
                            filterEmptyCategories(categoryList).map { category ->
                                mapCategory(category)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun mapCategory(category: Category) = CategoryItemViewAdapter(
        count = category.availableVoucherCount.formatNumber(),
        iconName = category.categoryIcon,
        title = category.categoryShortLabel
    )

    private fun bindViewToData() {
        viewModel.eventsHelper.stateData?.let { categories ->
            CategoriesViewAdapter(
                filterEmptyCategories(categories).map { category ->
                    mapCategory(category)
                }
            ).apply {
                categoriesView?.fillViewWithData(this)
            }
        }
    }

    private val categoriesViewCallback: CategoriesViewCallback by lazy {
        object : CategoriesViewCallback {
            override fun onItemClickedAtIndex(index: Int) {
                viewModel.eventsHelper.stateData?.let { categories ->
                    filterEmptyCategories(categories)[index]
                        .let { category ->
                            VouchersListFragment.newInstance(
                                category
                            ).apply {
                                this.onDataRefreshed = {
                                    viewModel.callGetAvailableCategories()
                                }
                            }.show(
                                childFragmentManager.beginTransaction(),
                                VOUCHERS_LIST_FRAGMENT_TAG
                            )
                        }
                }
            }
        }
    }

    private fun filterEmptyCategories(categoryList: List<Category>) = categoryList.filter {
        it.availableVoucherCount > 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoriesView = null
    }
}