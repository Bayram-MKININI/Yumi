package net.noliaware.yumi.feature_profile.presentation.controllers

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
import net.noliaware.yumi.commun.USED_VOUCHERS_LIST_FRAGMENT_TAG
import net.noliaware.yumi.commun.presentation.views.DataValueView
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.inflate
import net.noliaware.yumi.commun.util.parseToLongDate
import net.noliaware.yumi.commun.util.redirectToLoginScreen
import net.noliaware.yumi.feature_categories.presentation.controllers.HomeFragmentViewModel
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView.CategoryItemViewAdapter
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
import net.noliaware.yumi.feature_profile.presentation.views.ProfileView
import net.noliaware.yumi.feature_profile.presentation.views.ProfileView.ProfileViewAdapter
import net.noliaware.yumi.feature_profile.presentation.views.ProfileView.ProfileViewCallback

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var profileView: ProfileView? = null
    private val viewModel by activityViewModels<HomeFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.profile_layout, false)?.apply {
            profileView = findViewById(R.id.profile_view)
            profileView?.callback = profileViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
        viewModel.callGetUserProfile()
    }

    private fun collectFlows() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.userProfileEventsHelper.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreen(sharedEvent)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.userProfileEventsHelper.stateFlow.collect { vmState ->
                vmState.data?.let { userProfile ->
                    bindViewToData(userProfile)
                }
            }
        }
    }

    private fun bindViewToData(userProfile: UserProfile) {

        ProfileViewAdapter().also {
            createUserDataViews(it, userProfile)
            createUsedCategoriesViews(it, userProfile)
            profileView?.fillViewWithData(it)
        }
    }

    private fun createUserDataViews(
        profileViewAdapter: ProfileViewAdapter,
        userProfile: UserProfile
    ) {

        DataValueView.DataValueViewAdapter(
            title = getString(R.string.login),
            value = userProfile.login.orEmpty()
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        DataValueView.DataValueViewAdapter(
            title = getString(R.string.surname),
            value = userProfile.lastName.orEmpty()
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        DataValueView.DataValueViewAdapter(
            title = getString(R.string.name),
            value = userProfile.firstName.orEmpty()
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        DataValueView.DataValueViewAdapter(
            title = getString(R.string.birth),
            value = getString(
                R.string.birth_data,
                parseToLongDate(userProfile.birthDate),
                userProfile.birthCity,
                userProfile.birthCountry
            )
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        userProfile.userJob?.let { userJob ->
            DataValueView.DataValueViewAdapter(
                title = getString(R.string.occupation),
                value = userJob
            ).also {
                profileViewAdapter.myDataAdapters.add(it)
            }
        }

        userProfile.userReferent?.let { userReferent ->
            DataValueView.DataValueViewAdapter(
                title = getString(R.string.referent),
                value = userReferent
            ).also {
                profileViewAdapter.myDataAdapters.add(it)
            }
        }

        DataValueView.DataValueViewAdapter(
            title = getString(R.string.phone_numbers),
            value = userProfile.cellPhoneNumber.orEmpty()
        ).also {
            profileViewAdapter.complementaryDataAdapters.add(it)
        }

        val address = StringBuilder().apply {
            append(userProfile.address)
            append(getString(R.string.new_line))
            if (userProfile.addressComplement?.isNotBlank() == true) {
                append(userProfile.addressComplement)
                append(getString(R.string.new_line))
            }
            append(userProfile.city)
            append(getString(R.string.new_line))
            append(userProfile.postCode)
            append(getString(R.string.new_line))
            append(userProfile.country)
        }.toString()

        DataValueView.DataValueViewAdapter(
            title = getString(R.string.address),
            value = address
        ).also {
            profileViewAdapter.complementaryDataAdapters.add(it)
        }
    }

    private fun createUsedCategoriesViews(
        profileViewAdapter: ProfileViewAdapter,
        userProfile: UserProfile
    ) {

        userProfile.categories.map { category ->
            CategoryItemViewAdapter(
                count = category.voucherCount,
                iconName = category.categoryIcon ?: "ic_food",
                title = category.categoryLabel
            ).also {
                profileViewAdapter.categoryItemViewAdapters.add(it)
            }
        }
    }

    private val profileViewCallback: ProfileViewCallback by lazy {
        object : ProfileViewCallback {

            override fun onCategoryClickedAtIndex(index: Int) {

                viewModel.userProfileEventsHelper.stateFlow.value.data?.categories?.getOrNull(index)
                    ?.let { category ->
                        UsedVouchersListFragment.newInstance(
                            category.categoryId,
                            category.categoryLabel
                        ).show(
                            childFragmentManager.beginTransaction(),
                            USED_VOUCHERS_LIST_FRAGMENT_TAG
                        )
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        profileView = null
    }
}