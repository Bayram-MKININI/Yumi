package net.noliaware.yumi.feature_categories.presentation.controllers

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.VOUCHER_CODE_DATA
import net.noliaware.yumi.commun.util.parseToLongDate
import net.noliaware.yumi.commun.util.weak
import net.noliaware.yumi.commun.util.withArgs
import net.noliaware.yumi.feature_categories.domain.model.VoucherCodeData
import net.noliaware.yumi.feature_categories.presentation.views.QrCodeView
import net.noliaware.yumi.feature_categories.presentation.views.QrCodeView.QrCodeViewAdapter
import net.noliaware.yumi.feature_categories.presentation.views.QrCodeView.QrCodeViewCallback


@AndroidEntryPoint
class QrCodeFragment : AppCompatDialogFragment() {

    companion object {
        fun newInstance(voucherCodeData: VoucherCodeData) =
            QrCodeFragment().withArgs(VOUCHER_CODE_DATA to voucherCodeData)
    }

    private var qrCodeView: QrCodeView? = null
    private val viewModel by viewModels<QrCodeFragmentViewModel>()
    var callback: QrCodeFragmentCallback? by weak()

    interface QrCodeFragmentCallback {
        fun handleDialogClosed(qrCodeUnlocked: Boolean)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.qr_code_layout, container, false).apply {
            qrCodeView = this as QrCodeView
            qrCodeView?.callback = qrCodeViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
        bindViewToData()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collect { vmState ->
                vmState.data?.let { qrCode ->
                    qrCodeView?.setQrCode(qrCode)
                }
            }
        }
    }

    private fun bindViewToData() {
        viewModel.voucherCodeData?.let { voucherCodeData ->
            qrCodeView?.fillViewWithData(
                QrCodeViewAdapter(
                    title = voucherCodeData.productLabel ?: "",
                    creationDate = parseToLongDate(voucherCodeData.voucherDate),
                    expiryDate = parseToLongDate(voucherCodeData.voucherExpiryDate)
                )
            )
        }
    }

    private val qrCodeViewCallback: QrCodeViewCallback by lazy {
        object : QrCodeViewCallback {
            override fun onBackButtonClicked() {
                dismissAllowingStateLoss()
            }

            override fun onUseVoucherButtonClicked() {
                qrCodeView?.revealQrCode()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        qrCodeView?.isQrCodeRevealed()?.let {
            callback?.handleDialogClosed(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        qrCodeView = null
    }
}