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
import net.noliaware.yumi.commun.util.redirectToLoginScreen
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.presentation.views.QrCodeView
import net.noliaware.yumi.feature_categories.presentation.views.QrCodeView.QrCodeViewCallback
import net.noliaware.yumi.feature_categories.presentation.views.VouchersDetailsView
import net.noliaware.yumi.feature_categories.presentation.views.VouchersDetailsView.VouchersDetailsViewAdapter

@AndroidEntryPoint
class QrCodeFragment : AppCompatDialogFragment() {

    private var qrCodeView: QrCodeView? = null
    private val viewModel by viewModels<QrCodeFragmentViewModel>()

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

    private val qrCodeViewCallback: QrCodeViewCallback by lazy {
        object : QrCodeViewCallback {
            override fun onBackButtonClicked() {
                dismissAllowingStateLoss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        qrCodeView = null
    }
}