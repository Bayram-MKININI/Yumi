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
import net.noliaware.yumi.commun.ACCOUNT_DATA
import net.noliaware.yumi.commun.util.formatNumber
import net.noliaware.yumi.commun.util.inflate
import net.noliaware.yumi.commun.util.withArgs
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesView
import net.noliaware.yumi.feature_login.domain.model.AccountData

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    companion object {
        fun newInstance(
            accountData: AccountData?
        ) = CategoriesFragment().withArgs(ACCOUNT_DATA to accountData)
    }

    private var categoriesView: CategoriesView? = null
    private val viewModel: CategoriesFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.categories_layout, false)?.apply {
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
        val viewPager = categoriesView?.getViewPager
        CategoriesFragmentStateAdapter(childFragmentManager, lifecycle).apply {
            viewPager?.adapter = this
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoriesView = null
    }
}

class CategoriesFragmentStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AvailableCategoriesFragment()
            }
            1 -> {
                UsedCategoriesFragment()
            }
            else -> {
                CancelledCategoriesFragment()
            }
        }
    }
}