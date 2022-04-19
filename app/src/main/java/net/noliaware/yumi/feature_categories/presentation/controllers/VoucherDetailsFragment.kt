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
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.presentation.views.VouchersDetailsView
import net.noliaware.yumi.feature_categories.presentation.views.VouchersDetailsView.VouchersDetailsViewAdapter

@AndroidEntryPoint
class VoucherDetailsFragment : AppCompatDialogFragment() {

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


    /*viewModel.generatedBitmapLiveData.observe(
    viewLifecycleOwner
    ) { bitmap ->
        vouchersDetailsView?.setVoucherBitmap(bitmap)
    }

     */
    /*voucherDetailsFragmentViewModel.generateQrCode(
    it.url,
    resources.displayMetrics.widthPixels
    )

     */

    private val vouchersDetailsViewCallback: VouchersDetailsView.VouchersDetailsViewCallback by lazy {
        object : VouchersDetailsView.VouchersDetailsViewCallback {
            override fun onBackButtonClicked() {
                dismissAllowingStateLoss()
            }

            override fun onUseVoucherButtonClicked() {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vouchersDetailsView = null
    }
}