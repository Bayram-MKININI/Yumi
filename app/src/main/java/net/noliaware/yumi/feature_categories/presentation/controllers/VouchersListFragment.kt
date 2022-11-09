package net.noliaware.yumi.feature_categories.presentation.controllers

import android.content.DialogInterface
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
import net.noliaware.yumi.commun.util.handleSharedEvent
import net.noliaware.yumi.commun.util.parseToShortDate
import net.noliaware.yumi.commun.util.redirectToLoginScreen
import net.noliaware.yumi.commun.util.withArgs
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.presentation.views.VoucherItemView.VoucherItemViewAdapter
import net.noliaware.yumi.feature_categories.presentation.views.VouchersListView
import net.noliaware.yumi.feature_categories.presentation.views.VouchersListView.VouchersListViewCallback

@AndroidEntryPoint
class VouchersListFragment : AppCompatDialogFragment() {

    companion object {
        fun newInstance(categoryId: String, categoryLabel: String) =
            VouchersListFragment().withArgs(
                CATEGORY_ID to categoryId,
                CATEGORY_LABEL to categoryLabel
            )
    }

    private var vouchersListView: VouchersListView? = null
    private val viewModel by viewModels<VouchersListFragmentViewModel>()
    var callback: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.vouchers_list_layout, container, false).apply {
            vouchersListView = this as VouchersListView
            vouchersListView?.callback = readMailViewCallback
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
                redirectToLoginScreen(sharedEvent)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.stateFlow.collect { vmState ->
                vmState.data?.let { voucherList ->
                    bindViewToData(voucherList)
                }
            }
        }
    }

    private fun bindViewToData(voucherList: List<Voucher>) {

        voucherList.map { voucher ->
            VoucherItemViewAdapter(
                title = voucher.productLabel.orEmpty(),
                expiryDate = getString(
                    R.string.expiry_date,
                    parseToShortDate(voucher.voucherExpiryDate)
                ),
                description = getString(R.string.retailer, voucher.retailerLabel)
            )
        }.also {
            vouchersListView?.fillViewWithData(viewModel.categoryLabel, it)
        }
    }

    private val readMailViewCallback: VouchersListViewCallback by lazy {
        object : VouchersListViewCallback {
            override fun onBackButtonClicked() {
                dismissAllowingStateLoss()
            }

            override fun onItemClickedAtIndex(index: Int) {

                viewModel.eventsHelper.stateFlow.value.data?.get(index)?.voucherId?.let { voucherId ->
                    VoucherDetailsFragment.newInstance(
                        voucherId
                    ).apply {
                        callback = {
                            viewModel.dataShouldRefresh = true
                            viewModel.callGetVoucherList()
                        }
                    }.show(
                        childFragmentManager.beginTransaction(),
                        VOUCHER_DETAILS_FRAGMENT_TAG
                    )
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (viewModel.dataShouldRefresh == true) {
            callback?.invoke()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vouchersListView = null
    }
}