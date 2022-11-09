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
import net.noliaware.yumi.commun.VOUCHERS_LIST_FRAGMENT_TAG
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.inflate
import net.noliaware.yumi.commun.util.redirectToLoginScreen
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesView
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesView.CategoriesViewCallback
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView.CategoryItemViewAdapter

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    companion object {
        fun newInstance(): CategoriesFragment = CategoriesFragment()
    }

    private var categoriesView: CategoriesView? = null
    private val viewModel by activityViewModels<HomeFragmentViewModel>()

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

    private val categoriesViewCallback: CategoriesViewCallback by lazy {
        object : CategoriesViewCallback {
            override fun onItemClickedAtIndex(index: Int) {

                viewModel.accountData?.categories?.get(index)?.let { category ->
                    VouchersListFragment.newInstance(
                        category.categoryId,
                        category.categoryLabel
                    ).apply {
                        callback = {
                        }
                    }.show(
                        childFragmentManager.beginTransaction(),
                        VOUCHERS_LIST_FRAGMENT_TAG
                    )
                }
            }
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
                vmState.data?.let { categoryList ->
                    categoriesView?.refreshCategoryList(categoryList.map { category ->
                        mapCategory(category)
                    })
                }
            }
        }
    }

    private fun mapCategory(category: Category) =
        CategoryItemViewAdapter(
            count = category.voucherCount,
            iconName = category.categoryIcon ?: "ic_food",
            title = category.categoryLabel
        )

    private fun bindViewToData() {

        CategoriesView.CategoriesViewAdapter(
            description = getString(R.string.categories_list),
            categoryItemViewAdapters = viewModel.accountData?.categories?.map { category ->
                mapCategory(category)
            }.orEmpty()
        ).apply {
            categoriesView?.fillViewWithData(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoriesView = null
    }
}