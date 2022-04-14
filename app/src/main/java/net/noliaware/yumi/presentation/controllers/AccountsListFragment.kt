package net.noliaware.yumi.presentation.controllers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import net.noliaware.yumi.R
import net.noliaware.yumi.presentation.views.AccountCategoryView
import net.noliaware.yumi.presentation.views.AccountItemView.AccountItemViewAdapter
import net.noliaware.yumi.presentation.views.AccountsListView

class AccountsListFragment : AppCompatDialogFragment() {

    private var accountsListView: AccountsListView? = null
    private val viewModel by viewModels<AccountsListFragmentViewModel>()

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

        viewModel.managedAccounts?.map { managedAccount ->

            AccountItemViewAdapter(
                title = "${managedAccount.title} ${managedAccount.firstName} ${managedAccount.lastName}",
                phoneNumber = "Tel: ${managedAccount.cellNumber}",
                lastLogin = managedAccount.login
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
            }
        }.also { accountItemViewAdapters ->
            accountItemViewAdapters?.let { accountsListView?.fillViewWithData(it) }
        }
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