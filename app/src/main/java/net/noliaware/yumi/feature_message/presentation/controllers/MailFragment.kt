package net.noliaware.yumi.feature_message.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.MESSAGE_ID
import net.noliaware.yumi.commun.READ_MESSAGE_FRAGMENT_TAG
import net.noliaware.yumi.commun.SEND_MESSAGES_FRAGMENT_TAG
import net.noliaware.yumi.commun.util.DataError
import net.noliaware.yumi.commun.util.UIEvent
import net.noliaware.yumi.commun.util.inflate
import net.noliaware.yumi.commun.util.withArgs
import net.noliaware.yumi.feature_message.domain.model.Message
import net.noliaware.yumi.feature_message.presentation.views.MailItemView.MailItemViewAdapter
import net.noliaware.yumi.feature_message.presentation.views.MailView

@AndroidEntryPoint
class MailFragment : Fragment() {

    private var mailView: MailView? = null
    private val viewModel by viewModels<MailFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.mail_layout, false)?.apply {
            mailView = this as MailView
            mailView?.callback = mailViewCallback
        }
    }

    private val mailViewCallback: MailView.MailViewCallback by lazy {
        object : MailView.MailViewCallback {
            override fun onItemClickedAtIndex(index: Int) {

                val messageId = viewModel.stateFlow.value.data?.get(index)?.messageId

                messageId?.let {
                    ReadInboxMailFragment().withArgs(
                        MESSAGE_ID to it
                    ).show(
                        requireActivity().supportFragmentManager.beginTransaction(),
                        READ_MESSAGE_FRAGMENT_TAG
                    )
                }
            }

            override fun onComposeButtonClicked() {
                SendMailFragment().show(
                    requireActivity().supportFragmentManager.beginTransaction(),
                    SEND_MESSAGES_FRAGMENT_TAG
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
    }

    private fun collectFlows() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewModel.eventFlow.collectLatest { sharedEvent ->

                when (sharedEvent) {

                    is UIEvent.ShowSnackBar -> {

                        val message =
                            when (sharedEvent.dataError) {
                                DataError.NETWORK_ERROR -> getString(R.string.error_no_network)
                                DataError.SYSTEM_ERROR -> getString(R.string.error_contact_support)
                                DataError.NONE -> ""
                            }

                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collect { vmState ->
                vmState.data?.let { messageList ->
                    bindViewToData(messageList)
                }
            }
        }
    }

    private fun bindViewToData(messageList: List<Message>) {
        messageList.map { message ->
            MailItemViewAdapter(
                subject = message.messageSubject,
                time = message.messageDate,
                body = message.messageFrom
            )
        }.also {
            mailView?.fillViewWithData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mailView = null
    }
}