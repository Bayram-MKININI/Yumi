package net.noliaware.yumi.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import net.noliaware.yumi.R
import net.noliaware.yumi.model.Category
import net.noliaware.yumi.model.DataManager
import net.noliaware.yumi.model.VOUCHERS_DETAILS_FRAGMENT_TAG
import net.noliaware.yumi.views.VoucherItemView.VoucherItemViewAdapter
import net.noliaware.yumi.views.VouchersListView
import net.noliaware.yumi.views.VouchersListView.VouchersListViewCallback

class VouchersListFragment : AppCompatDialogFragment() {

    private var category: Category? = null
    private var vouchersListView: VouchersListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    companion object {

        private const val CATEGORY_ID = "categoryId"

        fun newInstance(categoryId: String): VouchersListFragment {
            val fragment = VouchersListFragment()
            val args = Bundle()
            args.putString(CATEGORY_ID, categoryId)
            fragment.arguments = args
            return fragment
        }
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

        val selectedCategoryId = arguments?.getString(CATEGORY_ID)

        category = DataManager.get().getCategoryById(selectedCategoryId)

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