package net.noliaware.yumi.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.DateTime.LONG_DATE_WITH_DAY_FORMAT
import net.noliaware.yumi.commun.util.ViewModelState
import net.noliaware.yumi.commun.util.formatNumber
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.parseDateToFormat
import net.noliaware.yumi.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi.feature_login.domain.model.TFAMode
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
import net.noliaware.yumi.feature_profile.presentation.views.ProfileParentView
import net.noliaware.yumi.feature_profile.presentation.views.ProfileView.ProfileParentViewCallback
import net.noliaware.yumi.feature_profile.presentation.views.ProfileView.ProfileViewAdapter

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var profileDataParentView: ProfileParentView? = null
    private val args: UserProfileFragmentArgs by navArgs()
    private val viewModel by viewModels<UserProfileFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_layout, container, false).apply {
            profileDataParentView = this as ProfileParentView
            profileDataParentView?.getProfileView?.callback = profileParentViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.eventFlow.collectLatest { sharedEvent ->
                profileDataParentView?.activateLoading(false)
                handleSharedEvent(sharedEvent)
                redirectToLoginScreenFromSharedEvent(sharedEvent)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.stateFlow.collect { vmState ->
                when (vmState) {
                    is ViewModelState.LoadingState -> profileDataParentView?.activateLoading(true)
                    is ViewModelState.DataState -> vmState.data?.let { userProfile ->
                        profileDataParentView?.activateLoading(false)
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
                userProfile.birthDate?.parseDateToFormat(LONG_DATE_WITH_DAY_FORMAT),
                userProfile.birthCity
            ),
            phone = userProfile.cellPhoneNumber.orEmpty(),
            twoFactorAuthModeText = map2FAModeText(args.accountData.twoFactorAuthMode),
            twoFactorAuthModeActivated = map2FAModeActivation(args.accountData.twoFactorAuthMode),
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

    private fun map2FAModeText(
        twoFactorAuthMode: TFAMode
    ) = when (twoFactorAuthMode) {
        TFAMode.APP -> getString(R.string.bo_two_factor_auth_by_app)
        TFAMode.MAIL -> getString(R.string.bo_two_factor_auth_by_mail)
        else -> getString(R.string.bo_two_factor_auth_none)
    }

    private fun map2FAModeActivation(
        twoFactorAuthMode: TFAMode
    ) = when (twoFactorAuthMode) {
        TFAMode.APP -> true
        else -> false
    }

    private val profileParentViewCallback: ProfileParentViewCallback by lazy {
        object : ProfileParentViewCallback {
            override fun onGetCodeButtonClicked() {
                findNavController().navigate(
                    UserProfileFragmentDirections.actionUserProfileFragmentToBOSignInFragment()
                )
            }

            override fun onPrivacyPolicyButtonClicked() {
                findNavController().navigate(
                    UserProfileFragmentDirections.actionUserProfileFragmentToPrivacyPolicyFragment(
                        privacyPolicyUrl = args.accountData.privacyPolicyUrl,
                        isPrivacyPolicyConfirmationRequired = false
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        profileDataParentView = null
        super.onDestroyView()
    }
}