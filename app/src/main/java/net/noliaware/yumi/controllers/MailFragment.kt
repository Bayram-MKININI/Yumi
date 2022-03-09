package net.noliaware.yumi.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.noliaware.yumi.R
import net.noliaware.yumi.model.READ_MESSAGE_FRAGMENT_TAG
import net.noliaware.yumi.model.SEND_MESSAGES_FRAGMENT_TAG
import net.noliaware.yumi.utils.inflate
import net.noliaware.yumi.views.MailItemView.MailItemViewAdapter
import net.noliaware.yumi.views.MailView

class MailFragment : Fragment() {

    private var mailView: MailView? = null

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
                val fragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                val readMailFragment = ReadMailFragment.newInstance(index)
                readMailFragment.show(fragmentTransaction, READ_MESSAGE_FRAGMENT_TAG)
            }

            override fun onComposeButtonClicked() {
                val fragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                val readMailFragment = SendMailFragment()
                readMailFragment.show(fragmentTransaction, SEND_MESSAGES_FRAGMENT_TAG)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshAdapters()
    }

    private fun refreshAdapters() {

        val mailItemViewAdaptersList = mutableListOf<MailItemViewAdapter>()

        MailItemViewAdapter(
            subject = "Thank you so much",
            time = "02-12-2021",
            body = "You will join a distributed and diverse Design team. You will be part of a team that includes Product, Design, and Engineering counterparts and will have direct impact throughout the product development lifecycle."
        ).also {
            mailItemViewAdaptersList.add(it)
        }

        MailItemViewAdapter(
            subject = "Soon as possible",
            time = "02-12-2021",
            body = "We are looking for a Product Designer to join our Marketplace team to work on developing new ways for customers to find and integrate third-party solutions into your Auth0 applications. We are looking for a designer that is a team player and works closely with engineers and product managers to solve complex problems."
        ).also {
            mailItemViewAdaptersList.add(it)
        }

        MailItemViewAdapter(
            subject = "Black Friday",
            time = "02-12-2021",
            body = "Work cross-functionally with Product Managers and Engineers, prioritizing features and improvements, facilitating design discussions, and providing feedback for our Marketplace experience."
        ).also {
            mailItemViewAdaptersList.add(it)
        }

        mailView?.fillViewWithData(mailItemViewAdaptersList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mailView = null
    }
}