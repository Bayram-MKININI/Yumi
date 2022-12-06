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
import net.noliaware.yumi.commun.presentation.adapters.ListLoadStateAdapter
import net.noliaware.yumi.commun.util.ViewModelState
import net.noliaware.yumi.commun.util.handlePaginationError
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi.feature_categories.presentation.controllers.MainActivity
import net.noliaware.yumi.feature_login.presentation.adapters.ManagedAccountAdapter
import net.noliaware.yumi.feature_login.presentation.views.AccountsListView

@AndroidEntryPoint
class AccountsListFragment : AppCompatDialogFragment() {

    companion object {
        fun newInstance() = AccountsListFragment()
    }

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
            accountsListView?.managedAccountAdapter = ManagedAccountAdapter { userProfile ->
                userProfile.login?.let { viewModel.callSelectAccountForLogin(it) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
    }

    private fun collectFlows() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreenFromSharedEvent(sharedEvent)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.stateFlow.collect { vmState ->
                when (vmState) {
                    is ViewModelState.LoadingState -> Unit
                    is ViewModelState.DataState -> vmState.data?.let { accountData ->
                        activity?.finish()
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        intent.putExtra(ACCOUNT_DATA, accountData)
                        startActivity(intent)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            accountsListView?.managedAccountAdapter?.loadStateFlow?.collectLatest { loadState ->
                handlePaginationError(loadState)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.managedProfiles.collectLatest {
                accountsListView?.managedAccountAdapter?.withLoadStateFooter(
                    footer = ListLoadStateAdapter()
                )
                accountsListView?.managedAccountAdapter?.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        accountsListView = null
    }
}