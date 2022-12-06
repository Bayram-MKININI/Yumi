package net.noliaware.yumi.feature_profile.presentation.controllers

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
import net.noliaware.yumi.commun.CATEGORY_ID
import net.noliaware.yumi.commun.CATEGORY_LABEL
import net.noliaware.yumi.commun.VOUCHER_DETAILS_FRAGMENT_TAG
import net.noliaware.yumi.commun.presentation.adapters.ListLoadStateAdapter
import net.noliaware.yumi.commun.util.handlePaginationError
import net.noliaware.yumi.commun.util.withArgs
import net.noliaware.yumi.feature_categories.presentation.adapters.VoucherAdapter
import net.noliaware.yumi.feature_categories.presentation.controllers.VoucherDetailsFragment
import net.noliaware.yumi.feature_categories.presentation.views.VouchersListView
import net.noliaware.yumi.feature_categories.presentation.views.VouchersListView.VouchersListViewCallback

@AndroidEntryPoint
class UsedVouchersListFragment : AppCompatDialogFragment() {

    companion object {
        fun newInstance(categoryId: String, categoryLabel: String) =
            UsedVouchersListFragment().withArgs(
                CATEGORY_ID to categoryId,
                CATEGORY_LABEL to categoryLabel
            )
    }

    private var vouchersListView: VouchersListView? = null
    private val viewModel by viewModels<UsedVouchersListFragmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.vouchers_list_layout, container, false).apply {
            vouchersListView = this as VouchersListView
            vouchersListView?.callback = vouchersListViewCallback
            vouchersListView?.voucherAdapter = VoucherAdapter(UsedVoucherMapper()) { voucher ->
                VoucherDetailsFragment.newInstance(
                    voucher.voucherId,
                    true
                ).show(
                    childFragmentManager.beginTransaction(),
                    VOUCHER_DETAILS_FRAGMENT_TAG
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vouchersListView?.setTitle(viewModel.categoryLabel)
        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vouchersListView?.voucherAdapter?.loadStateFlow?.collectLatest { loadState ->
                handlePaginationError(loadState)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.vouchers.collectLatest {
                vouchersListView?.voucherAdapter?.withLoadStateFooter(
                    footer = ListLoadStateAdapter()
                )
                vouchersListView?.voucherAdapter?.submitData(it)
            }
        }
    }

    private val vouchersListViewCallback: VouchersListViewCallback by lazy {
        object : VouchersListViewCallback {
            override fun onBackButtonClicked() {
                dismissAllowingStateLoss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vouchersListView = null
    }
}