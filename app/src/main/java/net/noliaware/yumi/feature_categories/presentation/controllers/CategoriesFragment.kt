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
import net.noliaware.yumi.commun.CATEGORIES_DATA
import net.noliaware.yumi.commun.VOUCHERS_LIST_FRAGMENT_TAG
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesView
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesView.CategoriesViewCallback
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView.CategoryItemViewAdapter

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    companion object {
        fun newInstance(categories: List<Category>?) =
            CategoriesFragment().withArgs(CATEGORIES_DATA to categories)
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
    }

    private fun collectFlows() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.availableCategoriesEventsHelper.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreen(sharedEvent)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.availableCategoriesEventsHelper.stateFlow.collect { vmState ->
                when (vmState) {
                    is ViewModelState.LoadingState -> Unit
                    is ViewModelState.DataState -> vmState.data?.let { categoryList ->
                        categoriesView?.refreshCategoryList(categoryList.map { category ->
                            mapCategory(category)
                        })
                    }
                }
            }
        }
    }

    private fun mapCategory(category: Category) =
        CategoryItemViewAdapter(
            count = category.availableVoucherCount ?: 0,
            iconName = category.categoryIcon,
            title = category.categoryShortLabel
        )

    private fun bindViewToData() {
        CategoriesView.CategoriesViewAdapter(
            description = getString(R.string.categories_list),
            categoryItemViewAdapters = viewModel.categories.map { category ->
                mapCategory(category)
            }
        ).apply {
            categoriesView?.fillViewWithData(this)
        }
    }

    private val categoriesViewCallback: CategoriesViewCallback by lazy {
        object : CategoriesViewCallback {
            override fun onItemClickedAtIndex(index: Int) {
                viewModel.categories[index].let { category ->
                    VouchersListFragment.newInstance(
                        category.categoryId,
                        category.categoryLabel
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

    override fun onDestroyView() {
        super.onDestroyView()
        categoriesView = null
    }
}