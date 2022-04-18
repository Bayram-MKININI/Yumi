package net.noliaware.yumi.feature_message.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi.R
import net.noliaware.yumi.feature_message.presentation.views.SendMailView

@AndroidEntryPoint
class SendMailFragment : AppCompatDialogFragment() {

    private var sendMailView: SendMailView? = null
    private lateinit var mailFragmentViewModel: MailFragmentViewModel

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

        mailFragmentViewModel =
            ViewModelProvider(requireActivity())[MailFragmentViewModel::class.java]
        setUpEvents()
    }

    private fun setUpEvents() {
        /*mailFragmentViewModel.messageSent.observe(viewLifecycleOwner) {
            dismissAllowingStateLoss()
            Toast.makeText(context, getString(R.string.mail_sent), Toast.LENGTH_SHORT).show()
        }
        mailFragmentViewModel.errorSentResponseLiveData.observe(viewLifecycleOwner) { errorResponse ->
            dismissAllowingStateLoss()
            activity?.handleErrorResponse(errorResponse)
        }

         */
    }

    private val sendMailViewCallback: SendMailView.SendMailViewCallback by lazy {
        object : SendMailView.SendMailViewCallback {
            override fun onBackButtonClicked() {
                dismissAllowingStateLoss()
            }

            override fun onClearButtonClicked() {
                sendMailView?.clearMail()
            }

            override fun onSendMailClicked(subject: String, text: String) {
                //mailFragmentViewModel.sendMessageWebservice(subject, text)
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