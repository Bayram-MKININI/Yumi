package net.noliaware.yumi.feature_categories.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.FragmentKeys.REFRESH_VOUCHER_CATEGORY_LIST_REQUEST_KEY
import net.noliaware.yumi.commun.util.collectLifecycleAware
import net.noliaware.yumi.commun.util.formatNumber
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesView

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private var categoriesView: CategoriesView? = null
    private val args by navArgs<CategoriesFragmentArgs>()
    private val viewModel by viewModels<CategoriesFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.categories_layout,
        container,
        false
    )?.apply {
        categoriesView = this as CategoriesView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFragmentListener()
        setUserName()
        collectFlow()
        setUpViewPager()
    }

    private fun setUpFragmentListener() {
        setFragmentResultListener(
            REFRESH_VOUCHER_CATEGORY_LIST_REQUEST_KEY
        ) { _, _ ->
            viewModel.sendCategoriesListsRefreshedEvent()
        }
    }

    private fun setUserName() {
        args.accountData.let {
            categoriesView?.setUserData(it.helloMessage, it.userName)
        }
    }

    private fun collectFlow() {
        viewModel.badgeCountFlow.collectLifecycleAware(viewLifecycleOwner) { badgeCount ->
            categoriesView?.setAvailableVouchersBadgeValue(
                badgeCount.formatNumber(),
                resources.getQuantityString(
                    R.plurals.available_vouchers,
                    badgeCount
                )
            )
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
                0 -> AvailableCategoriesListFragment()
                1 -> UsedCategoriesListFragment()
                else -> CancelledCategoriesListFragment()
            }
        }
    }
}