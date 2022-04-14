package net.noliaware.yumi.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi.R
import net.noliaware.yumi.domain.model.Category
import net.noliaware.yumi.commun.VOUCHERS_DETAILS_FRAGMENT_TAG
import net.noliaware.yumi.commun.util.DataError
import net.noliaware.yumi.domain.model.Voucher
import net.noliaware.yumi.presentation.views.VoucherItemView.VoucherItemViewAdapter
import net.noliaware.yumi.presentation.views.VouchersListView
import net.noliaware.yumi.presentation.views.VouchersListView.VouchersListViewCallback
import org.koin.androidx.viewmodel.ext.android.viewModel

class VouchersListFragment : AppCompatDialogFragment() {

    private var category: Category? = null
    private var vouchersListView: VouchersListView? = null
    private val viewModel by viewModel<VouchersListFragmentViewModel>()

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
            vouchersListView?.callback = readMailViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
    }

    private fun collectFlows() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewModel.eventFlow.collectLatest { sharedEvent ->

                when (sharedEvent) {

                    is UIEvent.ShowSnackBar -> {

                        val message =
                            when (sharedEvent.dataError) {
                                DataError.NETWORK_ERROR -> getString(R.string.error_no_network)
                                DataError.SYSTEM_ERROR -> getString(R.string.error_contact_support)
                                DataError.NONE -> ""
                            }

                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewModel.stateFlow.collect { vmState ->
                vmState.data?.let { voucherList ->

                    bindViewToData(voucherList)

                    /*val fragmentTransaction = childFragmentManager.beginTransaction()
                    val accountsListFragment = AccountsListFragment()
                    accountsListFragment.show(fragmentTransaction, ACCOUNTS_LIST_FRAGMENT_TAG)

                     */
                }
            }
        }
    }

    private fun bindViewToData(voucherList: List<Voucher>) {

        val voucherItemViewAdaptersList = mutableListOf<VoucherItemViewAdapter>()

        /*for (voucher in it.vouchersList) {

            val voucherItemViewAdapter = VoucherItemViewAdapter().apply {
                title = voucher.title
                status = voucher.price
                description = voucher.description
            }

            voucherItemViewAdaptersList.add(voucherItemViewAdapter)
        }
         */

        VoucherItemViewAdapter(
            title = "Panier de légumes",
            status = "Vérification en cours",
            statusColor = ContextCompat.getColor(requireContext(), R.color.orange),
            description = "La taille et la composition du panier alimentaire sont adaptées aux préférences locales…"
        ).also {
            voucherItemViewAdaptersList.add(it)
        }

        VoucherItemViewAdapter(
            title = "Panier de viandes",
            status = "Validitée",
            statusColor = ContextCompat.getColor(requireContext(), R.color.green),
            description = "Le panier comprend du riz au beurre, du poulet seekh kebab, de la tomate frite, du egg poch, du vert…"
        ).also {
            voucherItemViewAdaptersList.add(it)
        }

        VoucherItemViewAdapter(
            title = "Panier de boissons",
            status = "Validitée",
            statusColor = ContextCompat.getColor(requireContext(), R.color.green),
            description = "Le panier comprend du boissons gazeuses ordinaires, les boissons aux fruits, les boissons non alcoolisées…"
        ).also {
            voucherItemViewAdaptersList.add(it)
        }

        VoucherItemViewAdapter(
            title = "Panier de produits laitiers",
            status = "Validitée",
            statusColor = ContextCompat.getColor(requireContext(), R.color.green),
            description = "Le panier comprend du lait, les fromages Le Lorem Ipsum est simplement du faux texte"
        ).also {
            voucherItemViewAdaptersList.add(it)
        }

        vouchersListView?.fillViewWithData(voucherItemViewAdaptersList)
    }

    private val readMailViewCallback: VouchersListViewCallback by lazy {
        object : VouchersListViewCallback {
            override fun onBackButtonClicked() {
                dismissAllowingStateLoss()
            }

            override fun onItemClickedAtIndex(index: Int) {

                val fragmentTransaction = childFragmentManager.beginTransaction()

                /*category?.let {
                    val vouchersDetailsFragment =
                        VouchersDetailsFragment.newInstance(it.vouchersList[index].id)
                    vouchersDetailsFragment.show(fragmentTransaction, VOUCHERS_DETAILS_FRAGMENT_TAG)
                }

                 */

                val vouchersDetailsFragment = VouchersDetailsFragment.newInstance("Test")
                vouchersDetailsFragment.show(fragmentTransaction, VOUCHERS_DETAILS_FRAGMENT_TAG)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vouchersListView = null
    }
}