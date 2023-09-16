package net.noliaware.yumi.feature_categories.presentation.controllers

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.Args.CATEGORY
import net.noliaware.yumi.commun.FragmentTags.VOUCHER_DETAILS_FRAGMENT_TAG
import net.noliaware.yumi.commun.presentation.adapters.ListLoadStateAdapter
import net.noliaware.yumi.commun.util.decorateText
import net.noliaware.yumi.commun.util.getColorCompat
import net.noliaware.yumi.commun.util.handlePaginationError
import net.noliaware.yumi.commun.util.withArgs
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.presentation.adapters.VoucherAdapter
import net.noliaware.yumi.feature_categories.presentation.mappers.CancelledVoucherMapper
import net.noliaware.yumi.feature_categories.presentation.views.CategoryUI
import net.noliaware.yumi.feature_categories.presentation.views.VouchersListView
import net.noliaware.yumi.feature_categories.presentation.views.VouchersListView.VouchersListViewAdapter
import net.noliaware.yumi.feature_categories.presentation.views.VouchersListView.VouchersListViewCallback

@AndroidEntryPoint
class CancelledVouchersListFragment : AppCompatDialogFragment() {

    companion object {
        fun newInstance(
            category: Category
        ) = CancelledVouchersListFragment().withArgs(CATEGORY to category)
    }

    private var vouchersListView: VouchersListView? = null
    private val viewModel by viewModels<CancelledVouchersListFragmentViewModel>()

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
            vouchersListView?.voucherAdapter = VoucherAdapter(
                color = viewModel.selectedCategory?.categoryColor ?: Color.TRANSPARENT,
                voucherMapper = CancelledVoucherMapper()
            ) { voucher ->
                VoucherDetailsFragment.newInstance(
                    categoryUI = CategoryUI(
                        categoryColor = viewModel.selectedCategory?.categoryColor,
                        categoryIcon = viewModel.selectedCategory?.categoryIcon,
                        categoryLabel = viewModel.selectedCategory?.categoryLabel
                    ),
                    voucherId = voucher.voucherId
                ).show(
                    childFragmentManager.beginTransaction(),
                    VOUCHER_DETAILS_FRAGMENT_TAG
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = getString(
            R.string.cancelled_vouchers_list,
            viewModel.selectedCategory?.categoryLabel.orEmpty()
        )
        vouchersListView?.fillViewWithData(
            VouchersListViewAdapter(
                title = title.decorateText(
                    coloredText1 = getString(R.string.cancelled).lowercase(),
                    color1 = context?.getColorCompat(R.color.colorPrimary) ?: Color.TRANSPARENT,
                    coloredText2 = viewModel.selectedCategory?.categoryLabel.orEmpty(),
                    color2 = viewModel.selectedCategory?.categoryColor ?: Color.TRANSPARENT
                ),
                color = viewModel.selectedCategory?.categoryColor ?: Color.TRANSPARENT,
                iconName = viewModel.selectedCategory?.categoryIcon
            )
        )
        vouchersListView?.setLoadingVisible(true)
        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vouchersListView?.voucherAdapter?.loadStateFlow?.collectLatest { loadState ->
                when {
                    handlePaginationError(loadState) -> vouchersListView?.stopLoading()
                    loadState.refresh is LoadState.NotLoading -> {
                        vouchersListView?.setLoadingVisible(false)
                    }
                    else -> Unit
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getVouchers()?.collectLatest {
                vouchersListView?.voucherAdapter?.withLoadStateFooter(
                    footer = ListLoadStateAdapter()
                )
                vouchersListView?.voucherAdapter?.submitData(it)
            }
        }
    }

    private val vouchersListViewCallback: VouchersListViewCallback by lazy {
        VouchersListViewCallback {
            dismissAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vouchersListView = null
    }
}