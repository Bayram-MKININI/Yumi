package net.noliaware.yumi.feature_categories.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.formatNumber
import net.noliaware.yumi.commun.util.inflate
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesView

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private var categoriesView: CategoriesView? = null
    private val viewModel by activityViewModels<CategoriesFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.categories_layout)?.apply {
            categoriesView = this as CategoriesView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserName()
        collectFlow()
        setUpViewPager()
    }

    private fun setUserName() {
        viewModel.accountData?.let {
            categoriesView?.setUserData(it.helloMessage, it.userName)
        }
    }

    private fun collectFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.badgeCountFlow.collect { badgeCount ->
                categoriesView?.setAvailableVouchersBadgeValue(
                    badgeCount.formatNumber(),
                    resources.getQuantityString(
                        R.plurals.available_vouchers,
                        badgeCount
                    )
                )
            }
        }
    }

    private fun setUpViewPager() {
        CategoriesFragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle).apply {
            categoriesView?.getViewPager?.adapter = this
        }
    }

    override fun onDestroyView() {
        categoriesView = null
        super.onDestroyView()
    }

    private class CategoriesFragmentStateAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount() = 3
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AvailableCategoriesFragment()
                1 -> UsedCategoriesFragment()
                else -> CancelledCategoriesFragment()
            }
        }
    }
}