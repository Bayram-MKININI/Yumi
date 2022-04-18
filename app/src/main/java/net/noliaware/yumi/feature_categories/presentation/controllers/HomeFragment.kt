package net.noliaware.yumi.feature_categories.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.CATEGORIES_DATA
import net.noliaware.yumi.commun.util.inflate
import net.noliaware.yumi.commun.util.withArgs
import net.noliaware.yumi.feature_message.presentation.controllers.MailFragment
import net.noliaware.yumi.feature_alerts.presentation.controllers.AlertsFragment
import net.noliaware.yumi.feature_profile.presentation.controllers.UserProfileFragment
import net.noliaware.yumi.feature_categories.presentation.views.HomeMenuView
import net.noliaware.yumi.feature_categories.presentation.views.HomeView

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var homeView: HomeView? = null
    private val viewModel by viewModels<HomeFragmentViewModel>()

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
                    replace(R.id.main_fragment_container, MailFragment())
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
                CategoriesFragment().withArgs(CATEGORIES_DATA to viewModel.accountData?.categories)
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