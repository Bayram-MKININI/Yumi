package net.noliaware.yumi.feature_categories.presentation.controllers

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.CATEGORY_UI
import net.noliaware.yumi.commun.QR_CODE_FRAGMENT_TAG
import net.noliaware.yumi.commun.VOUCHER_ID
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.domain.model.VoucherCodeData
import net.noliaware.yumi.feature_categories.domain.model.VoucherStateData
import net.noliaware.yumi.feature_categories.domain.model.VoucherStatus
import net.noliaware.yumi.feature_categories.domain.model.VoucherStatus.*
import net.noliaware.yumi.feature_categories.presentation.views.CategoryUI
import net.noliaware.yumi.feature_categories.presentation.views.VouchersDetailsContainerView
import net.noliaware.yumi.feature_categories.presentation.views.VouchersDetailsContainerView.VouchersDetailsViewAdapter
import net.noliaware.yumi.feature_categories.presentation.views.VouchersDetailsContainerView.VouchersDetailsViewCallback

@AndroidEntryPoint
class VoucherDetailsFragment : AppCompatDialogFragment() {

    companion object {
        fun newInstance(
            categoryUI: CategoryUI,
            voucherId: String
        ) = VoucherDetailsFragment().withArgs(
            CATEGORY_UI to categoryUI,
            VOUCHER_ID to voucherId
        )
    }

    private var vouchersDetailsContainerView: VouchersDetailsContainerView? = null
    private val viewModel by viewModels<VoucherDetailsFragmentViewModel>()
    var onDataRefreshed: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.vouchers_details_layout, container, false).apply {
            vouchersDetailsContainerView = this as VouchersDetailsContainerView
            vouchersDetailsContainerView?.callback = vouchersDetailsViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
        vouchersDetailsContainerView?.setUpViewLook(
            color = viewModel.categoryUI?.categoryColor ?: Color.TRANSPARENT,
            iconName = viewModel.categoryUI?.categoryIcon
        )
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            merge(
                viewModel.getVoucherEventsHelper.eventFlow,
                viewModel.getVoucherStateDataEventsHelper.eventFlow
            ).flowWithLifecycle(lifecycle).collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreenFromSharedEvent(sharedEvent)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getVoucherEventsHelper.stateFlow.flowWithLifecycle(lifecycle)
                .collect { vmState ->
                    when (vmState) {
                        is ViewModelState.LoadingState -> Unit
                        is ViewModelState.DataState -> vmState.data?.let { voucher ->
                            bindViewToData(voucher)
                        }
                    }
                }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getVoucherStateDataEventsHelper.stateFlow.flowWithLifecycle(lifecycle)
                .collect { vmState ->
                    when (vmState) {
                        is ViewModelState.LoadingState -> Unit
                        is ViewModelState.DataState -> vmState.data?.let { voucherStateData ->
                            handleVoucherStateDataUpdated(voucherStateData)
                        }
                    }
                }
        }
    }

    private fun bindViewToData(voucher: Voucher) {

        val retailerAddress = StringBuilder().apply {
            append(voucher.retailerAddress)
            append(getString(R.string.new_line))
            if (voucher.retailerAddressComplement?.isNotEmpty() == true) {
                append(voucher.retailerAddressComplement)
                append(getString(R.string.new_line))
            }
            append(voucher.retailerPostcode)
            append(" ")
            append(voucher.retailerCity)
        }.toString()

        vouchersDetailsContainerView?.fillViewWithData(
            VouchersDetailsViewAdapter(
                title = voucher.productLabel.orEmpty(),
                startDate = getString(
                    R.string.created_in_hyphen,
                    parseToShortDate(voucher.voucherDate)
                ),
                endDate = mapVoucherEndDate(voucher),
                partnerAvailable = voucher.partnerInfoText?.isNotEmpty() == true,
                partnerLabel = voucher.partnerInfoText,
                voucherDescription = voucher.productDescription,
                retailerLabel = voucher.retailerLabel.orEmpty(),
                retailerAddress = retailerAddress,
                displayVoucherActionNotAvailable = voucher.voucherStatus != USABLE,
                voucherStatus = mapVoucherStatus(voucher.voucherStatus)
            )
        )
    }

    private fun mapVoucherEndDate(voucher: Voucher) =
        when (voucher.voucherStatus) {
            USABLE -> getString(
                R.string.expiry_date_value, parseToShortDate(voucher.voucherExpiryDate)
            )
            CONSUMED -> getString(
                R.string.usage_date_value,
                parseToShortDate(voucher.voucherUseDate),
                parseTimeString(voucher.voucherUseTime)
            )
            CANCELLED -> getString(
                R.string.cancellation_date_value,
                parseToShortDate(voucher.voucherUseDate),
                parseTimeString(voucher.voucherUseTime)
            )
            else -> ""
        }

    private fun mapVoucherStatus(voucherStatus: VoucherStatus?) =
        when (voucherStatus) {
            CONSUMED -> getString(R.string.voucher_consumed)
            CANCELLED -> getString(R.string.voucher_canceled)
            INEXISTENT -> getString(R.string.voucher_inexistent)
            else -> ""
        }

    private fun handleVoucherStateDataUpdated(voucherStateData: VoucherStateData) {
        val updatedVoucher = viewModel.getVoucherEventsHelper.stateData?.copy(
            voucherStatus = voucherStateData.voucherStatus,
            voucherUseDate = voucherStateData.voucherUseDate,
            voucherUseTime = voucherStateData.voucherUseTime
        )
        updatedVoucher?.let { bindViewToData(it) }
    }

    private val vouchersDetailsViewCallback: VouchersDetailsViewCallback by lazy {
        object : VouchersDetailsViewCallback {
            override fun onBackButtonClicked() {
                dismissAllowingStateLoss()
            }

            override fun onPartnerInfoClicked() {
                viewModel.getVoucherEventsHelper.stateData?.let { voucher ->
                    voucher.partnerInfoURL?.let { url ->
                        context?.openWebPage(url)
                    }
                }
            }

            override fun onPhoneButtonClicked() {
                viewModel.getVoucherEventsHelper.stateData?.let { voucher ->
                    voucher.retailerCellPhoneNumber?.let { phone ->
                        context?.makeCall(phone)
                    }
                }
            }

            override fun onLocationClicked() {
                viewModel.getVoucherEventsHelper.stateData?.let { voucher ->
                    val latitude = voucher.retailerAddressLatitude
                    val longitude = voucher.retailerAddressLongitude
                    val label = voucher.retailerLabel
                    openMap(context, latitude, longitude, label)
                }
            }

            override fun onDisplayVoucherButtonClicked() {
                viewModel.getVoucherEventsHelper.stateData?.let { voucher ->
                    QrCodeFragment.newInstance(
                        viewModel.categoryUI,
                        VoucherCodeData(
                            voucherId = voucher.voucherId,
                            productLabel = voucher.productLabel,
                            voucherDate = voucher.voucherDate,
                            voucherExpiryDate = voucher.voucherExpiryDate,
                            voucherCode = voucher.voucherCode,
                            voucherCodeSize = resources.displayMetrics.widthPixels
                        )
                    ).apply {
                        handleDialogClosed = {
                            viewModel.getVoucherEventsHelper.stateData?.voucherId?.let {
                                viewModel.callGetVoucherStatusById(it)
                            }
                        }
                    }.show(
                        childFragmentManager.beginTransaction(),
                        QR_CODE_FRAGMENT_TAG
                    )
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.getVoucherStateDataEventsHelper.stateData?.let { voucherStateData ->
            if (voucherStateData.voucherStatus == CONSUMED) {
                onDataRefreshed?.invoke()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vouchersDetailsContainerView = null
    }
}