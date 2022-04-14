package net.noliaware.yumi.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import net.noliaware.yumi.R
import net.noliaware.yumi.domain.model.AlertPriority
import net.noliaware.yumi.presentation.views.AlertItemView.AlertItemViewAdapter
import net.noliaware.yumi.presentation.views.AlertsView
import java.text.SimpleDateFormat
import java.util.*

class AlertsFragment : Fragment() {

    private var alertsView: AlertsView? = null
    private lateinit var categoriesFragmentViewModel: CategoriesFragmentViewModel

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
        categoriesFragmentViewModel = ViewModelProvider(requireActivity())[CategoriesFragmentViewModel::class.java]
        refreshAdapters()
    }

    private fun refreshAdapters() {

        val alertItemViewAdaptersList = mutableListOf<AlertItemViewAdapter>()

        /*categoriesFragmentViewModel.alertsList.forEach { alert ->

            AlertItemViewAdapter(
                sender = alert.from,
                time = formatTimestampToString(alert.timestamp),
                body = alert.text
            ).also {
                alertItemViewAdaptersList.add(it)
            }
        }

         */

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

        alertsView?.fillViewWithData(alertItemViewAdaptersList)
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