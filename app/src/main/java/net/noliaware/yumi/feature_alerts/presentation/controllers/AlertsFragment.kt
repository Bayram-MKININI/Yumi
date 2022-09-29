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
import net.noliaware.yumi.commun.util.redirectToLoginScreen
import net.noliaware.yumi.feature_alerts.domain.model.Alert
import net.noliaware.yumi.feature_alerts.domain.model.AlertPriority
import net.noliaware.yumi.feature_alerts.presentation.views.AlertItemView.AlertItemViewAdapter
import net.noliaware.yumi.feature_alerts.presentation.views.AlertsView
import net.noliaware.yumi.feature_categories.presentation.controllers.HomeFragmentViewModel
import java.text.SimpleDateFormat
import java.util.*

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
                priority = AlertPriority.getAlertPriorityByName("red"),
                sender = alert.alertDate,
                time = alert.alertTime,
                body = alert.alertText
            )
        }.also {
            alertsView?.fillViewWithData(it)
        }
/*
        AlertItemViewAdapter(
            priority = AlertPriority.getAlertPriorityByName("red"),
            sender = "Centre de santé",
            time = "02-12-2021",
            body = "Le patient ne répond pas au téléphone lorsqu’il a pris un rendez-vous de triage téléphonique avec un médecin généraliste ou une infirmière et ne contacte pas le cabinet à l’avance pour annuler/modifier le rendez-vous."
        ).also {
            alertItemViewAdaptersList.add(it)
        }

        AlertItemViewAdapter(
            priority = AlertPriority.getAlertPriorityByName("orange"),
            sender = "Covid-19",
            time = "08-12-2021",
            body = "Alors que la plupart des restrictions liées au Covid ont maintenant été levées, la pandémie n’est pas terminée. Notre équipe travaille dur pour essayer de s’assurer que nous continuons à fournir à nos patients de bons services de santé."
        ).also {
            alertItemViewAdaptersList.add(it)
        }

        AlertItemViewAdapter(
            sender = "Nouvelles mises à jour",
            time = "24-12-2021",
            body = "Vous pouvez recevoir une notification vous demandant si vous acceptez les nouvelles autorisations."
        ).also {
            alertItemViewAdaptersList.add(it)
        }
        */
    }

    private fun formatTimestampToString(timestamp: Long): String {
        val sdf = SimpleDateFormat(getString(R.string.mail_date_short_format), Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        alertsView = null
    }
}