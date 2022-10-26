package net.noliaware.yumi.feature_alerts.presentation.controllers

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
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.parseTimeString
import net.noliaware.yumi.commun.util.parseToShortDate
import net.noliaware.yumi.commun.util.redirectToLoginScreen
import net.noliaware.yumi.feature_alerts.domain.model.Alert
import net.noliaware.yumi.feature_alerts.domain.model.AlertPriority
import net.noliaware.yumi.feature_alerts.presentation.views.AlertItemView.AlertItemViewAdapter
import net.noliaware.yumi.feature_alerts.presentation.views.AlertsView
import net.noliaware.yumi.feature_categories.presentation.controllers.HomeFragmentViewModel

@AndroidEntryPoint
class AlertsFragment : Fragment() {

    private var alertsView: AlertsView? = null
    private val viewModel by activityViewModels<HomeFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.alerts_layout, container, false).apply {
            alertsView = this as AlertsView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
        viewModel.callGetAlertList()
    }

    private fun collectFlows() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.alertListEventsHelper.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreen(sharedEvent)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.alertListEventsHelper.stateFlow.collect { vmState ->
                vmState.data?.let { alertList ->
                    bindViewToData(alertList)
                }
            }
        }
    }

    private fun bindViewToData(alertList: List<Alert>) {

        alertList.map { alert ->
            AlertItemViewAdapter(
                priority = resolveAlertPriority(alert.alertLevel),
                time = getString(
                    R.string.received_at,
                    parseToShortDate(alert.alertDate),
                    parseTimeString(alert.alertTime)
                ),
                body = alert.alertText
            )
        }.also {
            alertsView?.fillViewWithData(it)
        }
    }

    private fun resolveAlertPriority(alertLevel: Int) = when (alertLevel) {
        2 -> AlertPriority.WARNING
        3 -> AlertPriority.IMPORTANT
        4 -> AlertPriority.CRITICAL
        else -> AlertPriority.INFORMATION
    }

    override fun onDestroyView() {
        super.onDestroyView()
        alertsView = null
    }
}