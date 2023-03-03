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
import net.noliaware.yumi.commun.CANCELLED_VOUCHERS_LIST_FRAGMENT_TAG
import net.noliaware.yumi.commun.util.ViewModelState
import net.noliaware.yumi.commun.util.formatNumber
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView.CategoryItemViewAdapter
import net.noliaware.yumi.feature_profile.presentation.views.ProfileCategoriesView
import net.noliaware.yumi.feature_profile.presentation.views.ProfileCategoriesView.ProfileCategoriesViewCallback

@AndroidEntryPoint
class CancelledCategoriesFragment : Fragment() {

    private var profileCategoriesView: ProfileCategoriesView? = null
    private val viewModel by viewModels<CancelledCategoriesFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_categories_layout, container, false).apply {
            profileCategoriesView = this as ProfileCategoriesView
            profileCategoriesView?.callback = categoriesViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileCategoriesView?.setViewTitle(getString(R.string.cancelled_vouchers))
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
        profileCategoriesView?.fillViewWithData(categoryItemViewAdapters)
    }

    private val categoriesViewCallback: ProfileCategoriesViewCallback by lazy {
        ProfileCategoriesViewCallback { index ->
            viewModel.eventsHelper.stateData?.let { categories ->
                categories[index].apply {
                    CancelledVouchersListFragment.newInstance(
                        this
                    ).show(
                        childFragmentManager.beginTransaction(),
                        CANCELLED_VOUCHERS_LIST_FRAGMENT_TAG
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        profileCategoriesView = null
    }
}