package net.noliaware.yumi.feature_categories.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.ACCOUNT_DATA
import net.noliaware.yumi.commun.util.inflate
import net.noliaware.yumi.commun.util.withArgs
import net.noliaware.yumi.feature_alerts.presentation.controllers.AlertsFragment
import net.noliaware.yumi.feature_categories.presentation.views.HomeMenuView
import net.noliaware.yumi.feature_categories.presentation.views.HomeView
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_message.presentation.controllers.MailFragment
import net.noliaware.yumi.feature_profile.presentation.controllers.UserProfileFragment

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        fun newInstance(accountData: AccountData): HomeFragment =
            HomeFragment().withArgs(ACCOUNT_DATA to accountData)
    }

    private var homeView: HomeView? = null
    private val viewModel by activityViewModels<HomeFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.home_layout, false)?.apply {
            homeView = this as HomeView
            homeView?.homeMenuView?.callback = homeMenuViewCallback
        }
    }

    private val homeMenuViewCallback: HomeMenuView.HomeMenuViewCallback by lazy {
        object : HomeMenuView.HomeMenuViewCallback {
            override fun onCategoryButtonClicked() {
                displayCategoriesFragment()
            }

            override fun onProfileButtonClicked() {
                childFragmentManager.beginTransaction().run {
                    replace(R.id.main_fragment_container, UserProfileFragment())
                    commitAllowingStateLoss()
                }
            }

            override fun onMailButtonClicked() {
                childFragmentManager.beginTransaction().run {
                    replace(
                        R.id.main_fragment_container,
                        MailFragment.newInstance(viewModel.accountData?.messageSubjects)
                    )
                    commitAllowingStateLoss()
                }
            }

            override fun onNotificationButtonClicked() {
                childFragmentManager.beginTransaction().run {
                    replace(R.id.main_fragment_container, AlertsFragment())
                    commitAllowingStateLoss()
                }
            }
        }
    }

    private fun displayCategoriesFragment() {
        childFragmentManager.beginTransaction().run {
            replace(
                R.id.main_fragment_container,
                CategoriesFragment.newInstance()
            )
            commitAllowingStateLoss()
            homeView?.selectHomeButton()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayCategoriesFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeView = null
    }
}