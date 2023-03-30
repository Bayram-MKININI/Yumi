package net.noliaware.yumi.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.BO_SIGN_IN_FRAGMENT_TAG
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
import net.noliaware.yumi.feature_profile.presentation.views.ProfileParentView
import net.noliaware.yumi.feature_profile.presentation.views.ProfileParentView.ProfileParentViewCallback
import net.noliaware.yumi.feature_profile.presentation.views.ProfileView.ProfileViewAdapter

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var profileDataParentView: ProfileParentView? = null
    private val viewModel by viewModels<UserProfileFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_layout, container, false).apply {
            profileDataParentView = this as ProfileParentView
            profileDataParentView?.callback = profileParentViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
    }

    private fun collectFlows() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.eventsHelper.eventFlow.flowWithLifecycle(lifecycle)
                .collectLatest { sharedEvent ->
                    handleSharedEvent(sharedEvent)
                    redirectToLoginScreenFromSharedEvent(sharedEvent)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.eventsHelper.stateFlow.flowWithLifecycle(lifecycle).collect { vmState ->
                when (vmState) {
                    is ViewModelState.LoadingState -> Unit
                    is ViewModelState.DataState -> vmState.data?.let { userProfile ->
                        bindViewToData(userProfile)
                    }
                }
            }
        }
    }

    private fun bindViewToData(userProfile: UserProfile) {
        val address = StringBuilder().apply {
            append(userProfile.address)
            append(getString(R.string.new_line))
            if (userProfile.addressComplement?.isNotBlank() == true) {
                append(userProfile.addressComplement)
                append(getString(R.string.new_line))
            }
            append(userProfile.postCode)
            append(" ")
            append(userProfile.city)
        }.toString()

        ProfileViewAdapter(
            login = userProfile.login.orEmpty(),
            surname = userProfile.lastName.orEmpty(),
            name = userProfile.firstName.orEmpty(),
            referent = userProfile.userReferent,
            birth = getString(
                R.string.birth_data,
                parseToLongDate(userProfile.birthDate),
                userProfile.birthCity
            ),
            phone = userProfile.cellPhoneNumber.orEmpty(),
            emittedValue = resources.getQuantityString(
                R.plurals.voucher_stat_format,
                userProfile.assignedVoucherCount,
                userProfile.assignedVoucherCount.formatNumber()
            ),
            availableValue = resources.getQuantityString(
                R.plurals.voucher_stat_format,
                userProfile.availableVoucherCount,
                userProfile.availableVoucherCount.formatNumber()
            ),
            usedValue = resources.getQuantityString(
                R.plurals.voucher_stat_format,
                userProfile.usedVoucherCount,
                userProfile.usedVoucherCount.formatNumber()
            ),
            cancelledValue = resources.getQuantityString(
                R.plurals.voucher_stat_format,
                userProfile.cancelledVoucherCount,
                userProfile.cancelledVoucherCount.formatNumber()
            ),
            address = address
        ).also {
            profileDataParentView?.getProfileView?.fillViewWithData(it)
        }
    }

    private val profileParentViewCallback: ProfileParentViewCallback by lazy {
        ProfileParentViewCallback {
            BOSignInFragment.newInstance().show(
                childFragmentManager.beginTransaction(),
                BO_SIGN_IN_FRAGMENT_TAG
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        profileDataParentView = null
    }
}