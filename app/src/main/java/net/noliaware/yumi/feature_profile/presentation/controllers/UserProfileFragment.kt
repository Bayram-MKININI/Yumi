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
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.inflate
import net.noliaware.yumi.commun.util.redirectToLoginScreen
import net.noliaware.yumi.feature_categories.presentation.controllers.VouchersListFragment
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView.CategoryItemViewAdapter
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
import net.noliaware.yumi.feature_profile.presentation.views.ProfileDataView.ProfileDataViewAdapter
import net.noliaware.yumi.feature_profile.presentation.views.ProfileView
import net.noliaware.yumi.feature_profile.presentation.views.ProfileView.ProfileViewAdapter
import net.noliaware.yumi.feature_profile.presentation.views.ProfileView.ProfileViewCallback

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var profileView: ProfileView? = null
    private val viewModel by viewModels<UserProfileFragmentViewModel>()

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
    }

    private fun collectFlows() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreen(sharedEvent)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collect { vmState ->
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

        ProfileDataViewAdapter(
            title = getString(R.string.surname),
            value = userProfile.lastName ?: ""
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.name),
            value = userProfile.firstName ?: ""
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.birth),
            value = getString(R.string.birth_data, userProfile.birthDate, userProfile.birthCity)
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.occupation),
            value = userProfile.userJob ?: ""
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.phone_numbers),
            value = userProfile.cellPhoneNumber ?: ""
        ).also {
            profileViewAdapter.complementaryDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.address),
            value = getString(
                R.string.address_data,
                userProfile.address,
                userProfile.addressComplement,
                userProfile.zipcode,
                userProfile.city
            )
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

                val categoryId = viewModel.stateFlow.value.data?.categories?.get(index)?.categoryId

                categoryId?.let {
                    VouchersListFragment.newInstance(it)
                        .show(
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