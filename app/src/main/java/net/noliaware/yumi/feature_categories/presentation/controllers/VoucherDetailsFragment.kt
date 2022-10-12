package net.noliaware.yumi.feature_categories.presentation.controllers

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
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.QR_CODE_FRAGMENT_TAG
import net.noliaware.yumi.commun.VOUCHER_ID
import net.noliaware.yumi.commun.presentation.views.DataValueView
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.openMap
import net.noliaware.yumi.commun.util.redirectToLoginScreen
import net.noliaware.yumi.commun.util.withArgs
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.presentation.views.VouchersDetailsView

@AndroidEntryPoint
class VoucherDetailsFragment : AppCompatDialogFragment() {

    companion object {
        fun newInstance(voucherId: String): VoucherDetailsFragment =
            VoucherDetailsFragment().withArgs(VOUCHER_ID to voucherId)
    }

    private var vouchersDetailsView: VouchersDetailsView? = null
    private val viewModel by viewModels<VoucherDetailsFragmentViewModel>()

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
            vouchersDetailsView = this as VouchersDetailsView
            vouchersDetailsView?.callback = vouchersDetailsViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
    }

    private fun collectFlows() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewModel.eventsHelper.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreen(sharedEvent)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.stateFlow.collect { vmState ->
                vmState.data?.let { voucher ->
                    bindViewToData(voucher)
                }
            }
        }
    }

    private fun bindViewToData(voucher: Voucher) {
        vouchersDetailsView?.addImageByDrawableName("ic_fruit_basket")
        vouchersDetailsView?.addTitle(voucher.productLabel ?: "")

        DataValueView.DataValueViewAdapter(
            title = getString(R.string.creation_date),
            value = voucher.voucherDate ?: ""
        ).also {
            vouchersDetailsView?.addDataValue(it)
        }

        DataValueView.DataValueViewAdapter(
            title = getString(R.string.expiry_date_title),
            value = voucher.voucherExpiryDate ?: ""
        ).also {
            vouchersDetailsView?.addDataValue(it)
        }

        if (!voucher.productDescription.isNullOrBlank()) {
            vouchersDetailsView?.addText(voucher.productDescription)
        }

        if (!voucher.productWebpage.isNullOrBlank()) {
            DataValueView.DataValueViewAdapter(
                title = getString(R.string.web_page),
                value = voucher.productWebpage
            ).also {
                vouchersDetailsView?.addDataValue(it)
            }
        }

        DataValueView.DataValueViewAdapter(
            title = getString(R.string.retailer_title),
            value = voucher.retailerLabel ?: ""
        ).also {
            vouchersDetailsView?.addDataValue(it)
        }

        val retailerAddress = StringBuilder().apply {
            append(voucher.retailerAddress)
            append(getString(R.string.new_line))
            if (voucher.retailerAddressComplement?.isNotBlank() == true) {
                append(voucher.retailerAddressComplement)
                append(getString(R.string.new_line))
            }
            append(voucher.retailerCity)
            append(getString(R.string.new_line))
            append(voucher.retailerPostcode)
            append(getString(R.string.new_line))
            append(voucher.retailerCountry)
        }.toString()

        DataValueView.DataValueViewAdapter(
            title = getString(R.string.address_title),
            value = retailerAddress
        ).also {
            vouchersDetailsView?.addDataValue(it)
        }

        DataValueView.DataValueViewAdapter(
            title = getString(R.string.mobile),
            value = voucher.retailerCellPhoneNumber ?: ""
        ).also {
            vouchersDetailsView?.addDataValue(it)
        }

        DataValueView.DataValueViewAdapter(
            title = getString(R.string.landline),
            value = voucher.retailerPhoneNumber ?: ""
        ).also {
            vouchersDetailsView?.addDataValue(it)
        }

        DataValueView.DataValueViewAdapter(
            title = getString(R.string.email),
            value = voucher.retailerEmail ?: ""
        ).also {
            vouchersDetailsView?.addDataValue(it)
        }

        if (!voucher.retailerWebsite.isNullOrBlank()) {
            DataValueView.DataValueViewAdapter(
                title = getString(R.string.web_page),
                value = voucher.retailerWebsite ?: ""
            ).also {
                vouchersDetailsView?.addDataValue(it)
            }
        }

        vouchersDetailsView?.addLocationView { vouchersDetailsViewCallback.onLocationClicked() }
    }

    private val vouchersDetailsViewCallback: VouchersDetailsView.VouchersDetailsViewCallback by lazy {
        object : VouchersDetailsView.VouchersDetailsViewCallback {
            override fun onBackButtonClicked() {
                dismissAllowingStateLoss()
            }

            override fun onLocationClicked() {
                viewModel.eventsHelper.stateFlow.value.data?.let { voucher ->
                    val latitude = voucher.retailerAddressLatitude
                    val longitude = voucher.retailerAddressLongitude
                    val label = voucher.retailerLabel
                    openMap(context, latitude, longitude, label)
                }
            }

            override fun onUseVoucherButtonClicked() {
                viewModel.eventsHelper.stateFlow.value.data?.voucherCode?.let { voucherCode ->
                    QrCodeFragment.newInstance(voucherCode, resources.displayMetrics.widthPixels)
                        .show(childFragmentManager.beginTransaction(), QR_CODE_FRAGMENT_TAG)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vouchersDetailsView = null
    }
}