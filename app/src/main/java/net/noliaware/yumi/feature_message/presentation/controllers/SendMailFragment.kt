package net.noliaware.yumi.feature_message.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.FragmentKeys.REFRESH_SENT_MESSAGES_REQUEST_KEY
import net.noliaware.yumi.commun.domain.model.Priority
import net.noliaware.yumi.commun.presentation.mappers.PriorityMapper
import net.noliaware.yumi.commun.util.ViewModelState
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.navDismiss
import net.noliaware.yumi.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi.feature_message.presentation.adapters.MessagePriorityAdapter
import net.noliaware.yumi.feature_message.presentation.adapters.MessageSubjectsAdapter
import net.noliaware.yumi.feature_message.presentation.views.PriorityUI
import net.noliaware.yumi.feature_message.presentation.views.SendMailView
import net.noliaware.yumi.feature_message.presentation.views.SendMailView.SendMailViewCallback

@AndroidEntryPoint
class SendMailFragment : AppCompatDialogFragment() {

    private var sendMailView: SendMailView? = null
    private val args: SendMailFragmentArgs by navArgs()
    private val viewModel by viewModels<SendMailFragmentViewModel>()

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
        setUpSubjectDropdownView()
        setUpPriorityDropdownView()
        setUpDefaultValuesIfAny()
        collectFlows()
    }

    private fun setUpSubjectDropdownView() {
        args.subjects?.map { messageSubject ->
            messageSubject.subjectLabel
        }.also { subjects ->
            sendMailView?.subjectSpinner?.adapter = MessageSubjectsAdapter(
                requireContext(),
                mutableListOf<String>().apply {
                    subjects?.let {
                        addAll(it)
                    }
                    add(getString(R.string.select_subject))
                }.toMutableList()
            )
            sendMailView?.subjectSpinner?.setSelection(
                sendMailView?.subjectSpinner?.adapter?.count ?: 0
            )
        }
    }

    private fun setUpPriorityDropdownView() {
        Priority.values().map { priority ->
            val mapper = PriorityMapper()
            PriorityUI(
                resIcon = mapper.mapPriorityIcon(priority),
                label = getString(mapper.mapPriorityTitle(priority))
            )
        }.also { priorities ->
            sendMailView?.prioritySpinner?.adapter = MessagePriorityAdapter(
                requireContext(),
                priorities
            )
        }
    }

    private fun setUpDefaultValuesIfAny() {
        args.message?.let { selectedMessage ->
            sendMailView?.setSubjectFixed(selectedMessage.messageSubject)
            sendMailView?.setPriorityFixed(PriorityMapper().mapPriorityIcon(selectedMessage.messagePriority))
        }
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.messageSentEventsHelper.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreenFromSharedEvent(sharedEvent)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.messageSentEventsHelper.stateFlow.collectLatest { vmState ->
                when (vmState) {
                    is ViewModelState.LoadingState -> Unit
                    is ViewModelState.DataState -> vmState.data?.let { result ->
                        if (result) {
                            setFragmentResult(
                                REFRESH_SENT_MESSAGES_REQUEST_KEY,
                                bundleOf()
                            )
                            navDismiss()
                        }
                    }
                }
            }
        }
    }

    private val sendMailViewCallback: SendMailViewCallback by lazy {
        object : SendMailViewCallback {
            override fun onBackButtonClicked() {
                navDismiss()
            }

            override fun onClearButtonClicked() {
                sendMailView?.clearMail()
            }

            override fun onSendMailClicked(text: String) {
                args.message?.let {
                    sendMailReply(text)
                } ?: run {
                    val selectedPriorityIndex = sendMailView?.getSelectedPriorityIndex() ?: 0
                    val priority = Priority.values()[selectedPriorityIndex].value
                    sendNewMail(priority, text)
                }
            }
        }
    }

    private fun sendMailReply(text: String) {
        viewModel.callSendMessage(
            messageId = args.message?.messageId,
            messageBody = text
        )
    }

    private fun sendNewMail(priority: Int, text: String) {
        val selectedSubjectIndex = sendMailView?.getSelectedSubjectIndex() ?: -1
        if (selectedSubjectIndex == -1) {
            return
        }
        args.subjects?.get(selectedSubjectIndex)?.let { messageSubject ->
            viewModel.callSendMessage(
                messagePriority = priority,
                messageSubjectId = messageSubject.subjectId.toString(),
                messageBody = text
            )
        }
    }

    override fun onResume() {
        super.onResume()
        sendMailView?.computeMailView()
    }

    override fun onDestroyView() {
        sendMailView = null
        super.onDestroyView()
    }
}