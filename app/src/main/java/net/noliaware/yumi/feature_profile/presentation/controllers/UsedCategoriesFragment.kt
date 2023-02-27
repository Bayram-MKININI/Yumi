package net.noliaware.yumi.feature_profile.presentation.controllers

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
import net.noliaware.yumi.commun.USED_VOUCHERS_LIST_FRAGMENT_TAG
import net.noliaware.yumi.commun.util.ViewModelState
import net.noliaware.yumi.commun.util.formatNumber
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView.CategoryItemViewAdapter
import net.noliaware.yumi.feature_profile.presentation.views.UsedCategoriesView
import net.noliaware.yumi.feature_profile.presentation.views.UsedCategoriesView.UsedCategoriesViewCallback

@AndroidEntryPoint
class UsedCategoriesFragment : Fragment() {

    private var usedCategoriesView: UsedCategoriesView? = null
    private val viewModel by viewModels<UsedCategoriesFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.used_categories_layout, container, false).apply {
            usedCategoriesView = this as UsedCategoriesView
            usedCategoriesView?.callback = profileViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
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
                    is ViewModelState.DataState -> vmState.data?.let { usedCategories ->
                        bindViewToData(usedCategories)
                    }
                }
            }
        }
    }

    private fun bindViewToData(usedCategories: List<Category>) {
        val categoryItemViewAdapters = mutableListOf<CategoryItemViewAdapter>()
        usedCategories.map { category ->
            CategoryItemViewAdapter(
                count = category.usedVoucherCount.formatNumber(),
                iconName = category.categoryIcon.orEmpty(),
                title = category.categoryShortLabel
            ).also {
                categoryItemViewAdapters.add(it)
            }
        }
        usedCategoriesView?.fillViewWithData(categoryItemViewAdapters)
    }

    private val profileViewCallback: UsedCategoriesViewCallback by lazy {
        object : UsedCategoriesViewCallback {
            override fun onCategoryClickedAtIndex(index: Int) {
                viewModel.eventsHelper.stateData?.let { categories ->
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        usedCategoriesView = null
    }
}