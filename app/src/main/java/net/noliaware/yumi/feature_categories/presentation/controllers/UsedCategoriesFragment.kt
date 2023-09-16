package net.noliaware.yumi.feature_categories.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.FragmentTags.USED_VOUCHERS_LIST_FRAGMENT_TAG
import net.noliaware.yumi.commun.util.ViewModelState
import net.noliaware.yumi.commun.util.formatNumber
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesListView
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView.CategoryItemViewAdapter

@AndroidEntryPoint
class UsedCategoriesFragment : Fragment() {

    private var categoriesListView: CategoriesListView? = null
    private val viewModel by activityViewModels<CategoriesFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.categories_list_layout, container, false).apply {
            categoriesListView = this as CategoriesListView
            categoriesListView?.callback = categoriesListViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
        viewModel.callGetUsedCategories()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.onDataRefreshedEventFlow.collectLatest {
                viewModel.callGetUsedCategories()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.usedCategoriesEventsHelper.eventFlow.collectLatest { sharedEvent ->
                categoriesListView?.stopLoading()
                handleSharedEvent(sharedEvent)
                redirectToLoginScreenFromSharedEvent(sharedEvent)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.usedCategoriesEventsHelper.stateFlow.collect { vmState ->
                when (vmState) {
                    is ViewModelState.LoadingState -> categoriesListView?.setLoadingVisible(true)
                    is ViewModelState.DataState -> vmState.data?.let { categories ->
                        categoriesListView?.setLoadingVisible(false)
                        bindViewToData(categories)
                    }
                }
            }
        }
    }

    private fun bindViewToData(categories: List<Category>) {
        val categoryItemViewAdapters = mutableListOf<CategoryItemViewAdapter>()
        categories.map { category ->
            CategoryItemViewAdapter(
                count = category.usedVoucherCount.formatNumber(),
                iconName = category.categoryIcon.orEmpty(),
                title = category.categoryShortLabel
            ).also {
                categoryItemViewAdapters.add(it)
            }
        }
        categoriesListView?.fillViewWithData(categoryItemViewAdapters)
    }

    private val categoriesListViewCallback: CategoriesListView.CategoriesListViewCallback by lazy {
        CategoriesListView.CategoriesListViewCallback { index ->
            viewModel.usedCategoriesEventsHelper.stateData?.let { categories ->
                categories[index].apply {
                    UsedVouchersListFragment.newInstance(
                        this
                    ).show(
                        childFragmentManager.beginTransaction(),
                        USED_VOUCHERS_LIST_FRAGMENT_TAG
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        categoriesListView = null
        super.onDestroyView()
    }
}