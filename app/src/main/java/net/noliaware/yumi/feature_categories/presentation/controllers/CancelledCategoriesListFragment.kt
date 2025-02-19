package net.noliaware.yumi.feature_categories.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.ViewState.DataState
import net.noliaware.yumi.commun.util.ViewState.LoadingState
import net.noliaware.yumi.commun.util.collectLifecycleAware
import net.noliaware.yumi.commun.util.formatNumber
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi.commun.util.safeNavigate
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesListView
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesListView.CategoriesListViewCallback
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView.CategoryItemViewAdapter

@AndroidEntryPoint
class CancelledCategoriesListFragment : Fragment() {

    private var categoriesListView: CategoriesListView? = null
    private val viewModel by viewModels<CategoriesFragmentViewModel>(
        ownerProducer = {
            requireParentFragment()
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.categories_list_layout,
        container,
        false
    ).apply {
        categoriesListView = this as CategoriesListView
        categoriesListView?.callback = categoriesListViewCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesListView?.setLoadingVisible(true)
        collectFlows()
        viewModel.callGetCancelledCategories()
    }

    private fun collectFlows() {
        viewModel.cancelledCategoriesEventsHelper.eventFlow.collectLifecycleAware(viewLifecycleOwner) { sharedEvent ->
            categoriesListView?.stopLoading()
            handleSharedEvent(sharedEvent)
            redirectToLoginScreenFromSharedEvent(sharedEvent)
        }
        viewModel.cancelledCategoriesEventsHelper.stateFlow.collectLifecycleAware(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is LoadingState -> Unit
                is DataState -> viewState.data?.let { categories ->
                    categoriesListView?.setLoadingVisible(false)
                    bindViewToData(categories)
                }
            }
        }
    }

    private fun bindViewToData(categories: List<Category>) {
        val categoryItemViewAdapters = mutableListOf<CategoryItemViewAdapter>()
        categories.map { category ->
            CategoryItemViewAdapter(
                count = category.cancelledVoucherCount.formatNumber(),
                iconName = category.categoryIcon.orEmpty(),
                title = category.categoryShortLabel
            ).also {
                categoryItemViewAdapters.add(it)
            }
        }
        categoriesListView?.fillViewWithData(categoryItemViewAdapters)
    }

    private val categoriesListViewCallback: CategoriesListViewCallback by lazy {
        CategoriesListViewCallback { index ->
            viewModel.cancelledCategoriesEventsHelper.stateData?.let { categories ->
                categories[index].apply {
                    findNavController().safeNavigate(
                        CategoriesFragmentDirections.actionCategoriesFragmentToCancelledVouchersListFragment(
                            this
                        )
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        categoriesListView?.callback = null
        categoriesListView = null
        super.onDestroyView()
    }
}