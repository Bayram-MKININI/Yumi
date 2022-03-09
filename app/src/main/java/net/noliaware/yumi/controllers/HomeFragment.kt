package net.noliaware.yumi.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import net.noliaware.yumi.R
import net.noliaware.yumi.utils.handleErrorResponse
import net.noliaware.yumi.utils.inflate
import net.noliaware.yumi.views.HomeMenuView.HomeMenuViewCallback
import net.noliaware.yumi.views.HomeView

class HomeFragment : Fragment() {

    private var homeView: HomeView? = null
    private lateinit var categoriesFragmentViewModel: CategoriesFragmentViewModel

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

    private val homeMenuViewCallback: HomeMenuViewCallback by lazy {
        object : HomeMenuViewCallback {
            override fun onCategoryButtonClicked() {
                displayCategoriesFragment()
            }

            override fun onProfileButtonClicked() {
                childFragmentManager.beginTransaction().run {
                    replace(R.id.main_fragment_container, ProfileFragment())
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
            replace(R.id.main_fragment_container, CategoriesFragment())
            commitAllowingStateLoss()
            homeView?.selectHomeButton()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesFragmentViewModel =
            ViewModelProvider(requireActivity())[CategoriesFragmentViewModel::class.java]
        setUpEvents()
        displayCategoriesFragment()
        categoriesFragmentViewModel.callGetAlertsWebservice()
    }

    private fun setUpEvents() {
        categoriesFragmentViewModel.liveAlertsList.observe(viewLifecycleOwner) { alertsList ->
            //refreshAdapters(messagesInboxList)
        }

        categoriesFragmentViewModel.errorInboxResponseLiveData.observe(viewLifecycleOwner) { errorResponse ->
            activity?.handleErrorResponse(errorResponse)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeView = null
    }
}