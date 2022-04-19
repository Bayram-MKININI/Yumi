package net.noliaware.yumi.feature_message.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.feature_message.domain.model.Message
import net.noliaware.yumi.feature_message.presentation.views.ReadMailView
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ReadOutboxMailFragment : AppCompatDialogFragment() {

    private var readMailView: ReadMailView? = null
    private val viewModel by viewModels<ReadOutboxMailFragmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
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
                vmState.data?.let { message ->
                    bindViewToData(message)
                }
            }
        }
    }

    private fun bindViewToData(message: Message) {
        ReadMailView.ReadMailViewAdapter(
            subject = message.messageSubject,
            time = message.messageDate,
            message = message.messageBody ?: ""
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