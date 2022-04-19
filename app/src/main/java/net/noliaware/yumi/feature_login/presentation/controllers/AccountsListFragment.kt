package net.noliaware.yumi.feature_login.presentation.controllers

import android.content.Intent
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
import net.noliaware.yumi.commun.ACCOUNT_DATA
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.feature_categories.presentation.controllers.MainActivity
import net.noliaware.yumi.feature_login.presentation.views.AccountCategoryView
import net.noliaware.yumi.feature_login.presentation.views.AccountItemView.AccountItemViewAdapter
import net.noliaware.yumi.feature_login.presentation.views.AccountsListView

@AndroidEntryPoint
class AccountsListFragment : AppCompatDialogFragment() {

    private var accountsListView: AccountsListView? = null
    private val viewModel: AccountsListFragmentViewModel by viewModels()

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
        collectFlows()
    }

    private fun refreshAdapters() {

        viewModel.managedProfiles?.map { managedProfile ->

            AccountItemViewAdapter(
                title = "${managedProfile.title} ${managedProfile.firstName} ${managedProfile.lastName}",
                phoneNumber = "Tel: ${managedProfile.cellPhoneNumber}",
                lastLogin = managedProfile.login ?: ""
            ).also { accountItemViewAdapter ->

                managedProfile.categories.map { category ->
                    AccountCategoryView.AccountCategoryViewAdapter(
                        iconName = category.categoryIcon ?: "ic_food",
                        title = category.categoryLabel,
                        count = category.voucherCount
                    )
                }.also {
                    accountItemViewAdapter.accountCategoryViewAdapters.addAll(it)
                }
            }

        }.also { accountItemViewAdapters ->
            accountItemViewAdapters?.let { accountsListView?.fillViewWithData(it) }
        }
    }

    private fun collectFlows() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewModel.stateFlow.collect { vmState ->
                vmState.data?.let { accountData ->

                    activity?.finish()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    intent.putExtra(ACCOUNT_DATA, accountData)
                    startActivity(intent)
                }
            }
        }
    }

    private val accountsListViewCallback: AccountsListView.AccountsListViewCallback by lazy {
        object : AccountsListView.AccountsListViewCallback {
            override fun onItemClickedAtIndex(index: Int) {

                viewModel.managedProfiles?.get(index)?.login?.let {
                    viewModel.callSelectAccountForLogin(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        accountsListView = null
    }
}