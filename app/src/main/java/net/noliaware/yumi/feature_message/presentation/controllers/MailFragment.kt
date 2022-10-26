package net.noliaware.yumi.feature_message.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.MESSAGE_SUBJECTS_DATA
import net.noliaware.yumi.commun.READ_MESSAGE_FRAGMENT_TAG
import net.noliaware.yumi.commun.SEND_MESSAGES_FRAGMENT_TAG
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_categories.presentation.controllers.HomeFragmentViewModel
import net.noliaware.yumi.feature_login.domain.model.MessageSubject
import net.noliaware.yumi.feature_message.domain.model.Message
import net.noliaware.yumi.feature_message.domain.model.MessageOrigin
import net.noliaware.yumi.feature_message.presentation.views.MailItemView.MailItemViewAdapter
import net.noliaware.yumi.feature_message.presentation.views.MailView

@AndroidEntryPoint
class MailFragment : Fragment() {

    companion object {
        fun newInstance(messageSubjects: List<MessageSubject>?): MailFragment =
            MailFragment().withArgs(MESSAGE_SUBJECTS_DATA to messageSubjects)
    }

    private var mailView: MailView? = null
    private val viewModel by activityViewModels<HomeFragmentViewModel>()

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

                val message = viewModel.messageListEventsHelper.stateFlow.value.data?.get(index)

                message?.let {

                    when (message.messageOrigin) {

                        MessageOrigin.INBOX -> {
                            ReadInboxMailFragment.newInstance(message.messageId)
                                .show(
                                    requireActivity().supportFragmentManager.beginTransaction(),
                                    READ_MESSAGE_FRAGMENT_TAG
                                )
                        }

                        MessageOrigin.OUTBOX -> {
                            ReadOutboxMailFragment.newInstance(message.messageId)
                                .show(
                                    requireActivity().supportFragmentManager.beginTransaction(),
                                    READ_MESSAGE_FRAGMENT_TAG
                                )
                        }
                        else -> Unit
                    }
                }
            }

            override fun onComposeButtonClicked() {
                SendMailFragment.newInstance(viewModel.messageSubjects)
                    .show(
                        requireActivity().supportFragmentManager.beginTransaction(),
                        SEND_MESSAGES_FRAGMENT_TAG
                    )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
        viewModel.callGetInboxMessageList()
    }

    private fun collectFlows() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.messageListEventsHelper.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreen(sharedEvent)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.messageListEventsHelper.stateFlow.collect { vmState ->
                vmState.data?.let { messageList ->
                    bindViewToData(messageList)
                }
            }
        }
    }

    private fun bindViewToData(messageList: List<Message>) {
        messageList.map { message ->
            MailItemViewAdapter(
                subject = message.messageFrom ?: "",
                time = parseToShortDate(message.messageDate),
                body = message.messageSubject
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