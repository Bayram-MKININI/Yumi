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
import net.noliaware.yumi.feature_message.presentation.views.ReadMailView
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ReadMailFragment : AppCompatDialogFragment() {

    private var readMailView: ReadMailView? = null
    private lateinit var viewModel: MailFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    companion object {

        private const val MESSAGE_INDEX = "messageIndex"

        fun newInstance(messageIndex: Int): ReadMailFragment {
            val fragment = ReadMailFragment()
            val args = Bundle()
            args.putInt(MESSAGE_INDEX, messageIndex)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.read_mail_layout, container, false).apply {
            readMailView = this as ReadMailView
            readMailView?.callback = readMailViewCallback
        }
    }

    private val readMailViewCallback: ReadMailView.ReadMailViewCallback by lazy {
        object : ReadMailView.ReadMailViewCallback {
            override fun onBackButtonClicked() {
                dismissAllowingStateLoss()
            }

            override fun onComposeButtonClicked() {
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[MailFragmentViewModel::class.java]

        /*
        val selectedIndex = arguments?.getInt(MESSAGE_INDEX, 0)
        val selectedMessage = viewModel.messagesInboxList[selectedIndex!!]

        ReadMailViewAdapter().apply {
            subject = selectedMessage.subject
            time = formatTimestampToString(selectedMessage.timestamp)
            message = selectedMessage.text
        }.also {
            //readMailView.fillViewWithData(it)
        }*/

        ReadMailView.ReadMailViewAdapter(
            subject = "Expéditeur 1",
            time = "Il y a deux jours",
            message = "Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations. Vous pouvez recevoir une notification vous demandant si " +
                    "vous acceptez les nouvelles autorisations. Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations." +
                    "Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations. Vous pouvez recevoir une notification vous demandant si " +
                    "vous acceptez les nouvelles autorisations. Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations." +
                    "Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations. Vous pouvez recevoir une notification vous demandant si " +
                    "vous acceptez les nouvelles autorisations. Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations." +
                    "Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations. Vous pouvez recevoir une notification vous demandant si " +
                    "vous acceptez les nouvelles autorisations. Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations." +
                    "Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations. Vous pouvez recevoir une notification vous demandant si " +
                    "vous acceptez les nouvelles autorisations. Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations." +
                    "Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations. Vous pouvez recevoir une notification vous demandant si " +
                    "vous acceptez les nouvelles autorisations. Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations." +
                    "Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations. Vous pouvez recevoir une notification vous demandant si " +
                    "vous acceptez les nouvelles autorisations. Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations."
        ).also {
            readMailView?.fillViewWithData(it)
        }
    }

    private fun formatTimestampToString(timestamp: Long): String {
        val sdf = SimpleDateFormat(getString(R.string.mail_date_long_format), Locale.US)
        val date = Date(timestamp)
        return sdf.format(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        readMailView = null
    }
}