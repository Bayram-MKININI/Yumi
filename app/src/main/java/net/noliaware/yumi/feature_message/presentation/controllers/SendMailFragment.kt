package net.noliaware.yumi.feature_message.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.redirectToLoginScreen
import net.noliaware.yumi.feature_message.presentation.views.SendMailView
import net.noliaware.yumi.feature_message.presentation.views.SendMailView.SendMailViewCallback

@AndroidEntryPoint
class SendMailFragment : AppCompatDialogFragment() {

    private var sendMailView: SendMailView? = null
    private val viewModel by viewModels<SendMailFragmentViewModel>()
    private var dialog: AlertDialog? = null
    private var selectedMessageIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.send_mail_layout, container, false).apply {
            sendMailView = this as SendMailView
            sendMailView?.callback = sendMailViewCallback
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
                vmState.data?.let { status ->
                    if (status)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.mail_sent),
                            Toast.LENGTH_LONG
                        ).show()
                }
            }
        }
    }

    private val sendMailViewCallback: SendMailViewCallback by lazy {
        object : SendMailViewCallback {
            override fun onBackButtonClicked() {
                dismissAllowingStateLoss()
            }

            override fun onSubjectEditTextClicked() {

                if (dialog?.isShowing == true)
                    return

                // setup the alert builder
                val builder = MaterialAlertDialogBuilder(
                    requireContext(),
                    R.style.ThemeOverlay_Material3_Dialog
                )
                builder.setTitle("Choose an animal")

                val messageSubjects: Array<String> =
                    viewModel.messageSubjects?.map { it.subjectLabel }?.toTypedArray()!!

                builder.setItems(messageSubjects) { _, which ->
                    selectedMessageIndex = which
                    sendMailView?.setSubject(messageSubjects[which])
                }

                dialog = builder.create()
                dialog?.show()
            }

            override fun onClearButtonClicked() {
                sendMailView?.clearMail()
            }

            override fun onSendMailClicked(text: String) {
                viewModel.messageSubjects?.get(selectedMessageIndex)?.let {
                    viewModel.callSendMessage(it.subjectId.toString(), text)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sendMailView?.computeMailView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sendMailView = null
    }
}