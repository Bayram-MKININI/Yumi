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
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.merge
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.CATEGORY_UI
import net.noliaware.yumi.commun.QR_CODE_FRAGMENT_TAG
import net.noliaware.yumi.commun.VOUCHER_ID
import net.noliaware.yumi.commun.VOUCHER_VALIDATED
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.domain.model.VoucherCodeData
import net.noliaware.yumi.feature_categories.domain.model.VoucherStatus
import net.noliaware.yumi.feature_categories.presentation.views.CategoryUI
import net.noliaware.yumi.feature_categories.presentation.views.VouchersDetailsContainerView
import net.noliaware.yumi.feature_categories.presentation.views.VouchersDetailsContainerView.VouchersDetailsViewAdapter
import net.noliaware.yumi.feature_categories.presentation.views.VouchersDetailsContainerView.VouchersDetailsViewCallback

@AndroidEntryPoint
class VoucherDetailsFragment : AppCompatDialogFragment() {

    companion object {
        fun newInstance(
            categoryUI: CategoryUI,
            voucherId: String,
            voucherValidated: Boolean = false
        ) = VoucherDetailsFragment().withArgs(
            CATEGORY_UI to categoryUI,
            VOUCHER_ID to voucherId,
            VOUCHER_VALIDATED to voucherValidated
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

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            merge(
                viewModel.getVoucherEventsHelper.eventFlow,
                viewModel.getVoucherStatusEventsHelper.eventFlow
            ).collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreenFromSharedEvent(sharedEvent)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getVoucherEventsHelper.stateFlow.collect { vmState ->
                when (vmState) {
                    is ViewModelState.LoadingState -> Unit
                    is ViewModelState.DataState -> vmState.data?.let { voucher ->
                        bindViewToData(voucher)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getVoucherStatusEventsHelper.stateFlow.collect { vmState ->
                when (vmState) {
                    is ViewModelState.LoadingState -> Unit
                    is ViewModelState.DataState -> vmState.data?.let { voucherStatus ->
                        handleVoucherStatusUpdate(voucherStatus)
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
                endDate = getString(
                    R.string.expiry_date_value,
                    parseToShortDate(voucher.voucherExpiryDate)
                ),
                partnerLabel = voucher.partnerInfoText,
                voucherDescription = voucher.productDescription,
                retailerLabel = voucher.retailerLabel.orEmpty(),
                retailerAddress = retailerAddress,
                displayVoucherActionNotAvailable = viewModel.voucherValidated == true
            )
        )

        if (viewModel.voucherValidated == true) {
            handleVoucherStatusUpdate(VoucherStatus.CONSUMED)
        }
    }

    private fun handleVoucherStatusUpdate(voucherStatus: VoucherStatus) {
        when (voucherStatus) {
            VoucherStatus.INEXISTENT -> vouchersDetailsContainerView?.setVoucherStatus(getString(R.string.voucher_inexistent))
            VoucherStatus.CANCELED -> vouchersDetailsContainerView?.setVoucherStatus(getString(R.string.voucher_canceled))
            VoucherStatus.USABLE -> Unit
            VoucherStatus.CONSUMED -> vouchersDetailsContainerView?.setVoucherStatus(getString(R.string.voucher_consumed))
        }
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
        viewModel.getVoucherStatusEventsHelper.stateData?.let { voucherStatus ->
            if (voucherStatus == VoucherStatus.CONSUMED) {
                onDataRefreshed?.invoke()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vouchersDetailsContainerView = null
    }
}