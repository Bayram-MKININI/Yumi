package net.noliaware.yumi.controllers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import net.noliaware.yumi.R
import net.noliaware.yumi.views.AccountCategoryView
import net.noliaware.yumi.views.AccountItemView.AccountItemViewAdapter
import net.noliaware.yumi.views.AccountsListView

class AccountsListFragment : AppCompatDialogFragment() {

    private var accountsListView: AccountsListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.accounts_list_layout, container, false).apply {
            accountsListView = this as AccountsListView
            accountsListView?.callback = accountsListViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshAdapters()
    }

    private fun refreshAdapters() {

        val accountItemViewAdapters = mutableListOf<AccountItemViewAdapter>()

        AccountItemViewAdapter(
            title = "Nathalie Duponds",
            phoneNumber = "Tel: 033 23 55 36 56",
            lastLogin = "Dernière connexion: Dec 2021"
        ).also {

            AccountCategoryView.AccountCategoryViewAdapter(
                iconName = "ic_food",
                title = "Alimentaire:",
                count = 5
            ).also { accountCategoryViewAdapter ->
                it.accountCategoryViewAdapters.add(accountCategoryViewAdapter)
            }

            AccountCategoryView.AccountCategoryViewAdapter(
                iconName = "ic_energy",
                title = "Energie:",
                count = 4
            ).also { accountCategoryViewAdapter ->
                it.accountCategoryViewAdapters.add(accountCategoryViewAdapter)
            }

            AccountCategoryView.AccountCategoryViewAdapter(
                iconName = "ic_health",
                title = "Santé:",
                count = 5
            ).also { accountCategoryViewAdapter ->
                it.accountCategoryViewAdapters.add(accountCategoryViewAdapter)
            }

            accountItemViewAdapters.add(it)
        }

        AccountItemViewAdapter(
            title = "Jean Dupont",
            phoneNumber = "Tel: 06 23 56 89 56",
            lastLogin = "Dernière connexion: Oct 2021"
        ).also {

            AccountCategoryView.AccountCategoryViewAdapter(
                iconName = "ic_food",
                title = "Alimentaire:",
                count = 15
            ).also { accountCategoryViewAdapter ->
                it.accountCategoryViewAdapters.add(accountCategoryViewAdapter)
            }

            AccountCategoryView.AccountCategoryViewAdapter(
                iconName = "ic_energy",
                title = "Energie:",
                count = 4
            ).also { accountCategoryViewAdapter ->
                it.accountCategoryViewAdapters.add(accountCategoryViewAdapter)
            }

            AccountCategoryView.AccountCategoryViewAdapter(
                iconName = "ic_services",
                title = "Service:",
                count = 15
            ).also { accountCategoryViewAdapter ->
                it.accountCategoryViewAdapters.add(accountCategoryViewAdapter)
            }

            accountItemViewAdapters.add(it)
        }

        accountsListView?.fillViewWithData(accountItemViewAdapters)
    }

    private val accountsListViewCallback: AccountsListView.AccountsListViewCallback by lazy {
        object : AccountsListView.AccountsListViewCallback {
            override fun onItemClickedAtIndex(index: Int) {
                activity?.finish()
                startActivity(
                    Intent(context, MainActivity::class.java)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        accountsListView = null
    }
}