package net.noliaware.yumi.feature_categories.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.QR_CODE_FRAGMENT_TAG
import net.noliaware.yumi.commun.VOUCHER_ID
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.redirectToLoginScreen
import net.noliaware.yumi.commun.util.withArgs
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.presentation.views.VouchersDetailsView
import net.noliaware.yumi.feature_categories.presentation.views.VouchersDetailsView.VouchersDetailsViewAdapter

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

            viewModel.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreen(sharedEvent)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collect { vmState ->
                vmState.data?.let { voucher ->
                    bindViewToData(voucher)
                }
            }
        }
    }

    private fun bindViewToData(voucher: Voucher) {

        VouchersDetailsViewAdapter(
            iconName = "ic_fruit_basket",
            title = voucher.productLabel ?: "",
            status = "Vérification en cours",
            statusColor = ContextCompat.getColor(requireContext(), R.color.orange),
            description = voucher.productDescription ?: ""
        ).also {
            vouchersDetailsView?.fillViewWithData(it)
        }
    }

    private val vouchersDetailsViewCallback: VouchersDetailsView.VouchersDetailsViewCallback by lazy {
        object : VouchersDetailsView.VouchersDetailsViewCallback {
            override fun onBackButtonClicked() {
                dismissAllowingStateLoss()
            }

            override fun onUseVoucherButtonClicked() {
                viewModel.stateFlow.value.data?.voucherCode?.let { voucherCode ->
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