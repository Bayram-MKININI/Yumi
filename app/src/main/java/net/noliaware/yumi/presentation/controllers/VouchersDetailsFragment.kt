package net.noliaware.yumi.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import net.noliaware.yumi.R
import net.noliaware.yumi.presentation.views.VouchersDetailsView
import net.noliaware.yumi.presentation.views.VouchersDetailsView.VouchersDetailsViewAdapter

class VouchersDetailsFragment : AppCompatDialogFragment() {

    private var vouchersDetailsView: VouchersDetailsView? = null
    private lateinit var voucherDetailsFragmentViewModel: VoucherDetailsFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    companion object {

        private const val VOUCHER_ID = "voucherId"

        fun newInstance(voucherId: String): VouchersDetailsFragment {
            val fragment = VouchersDetailsFragment()
            val args = Bundle()
            args.putString(VOUCHER_ID, voucherId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.vouchers_details_layout, container, false).apply {
            vouchersDetailsView = this as VouchersDetailsView
            vouchersDetailsView?.callback = vouchersDetailsViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedVoucherId = arguments?.getString(VOUCHER_ID) ?: ""

        voucherDetailsFragmentViewModel =
            ViewModelProvider(requireActivity())[VoucherDetailsFragmentViewModel::class.java]

        voucherDetailsFragmentViewModel.generatedBitmapLiveData.observe(
            viewLifecycleOwner
        ) { bitmap ->
            vouchersDetailsView?.setVoucherBitmap(bitmap)
        }

        /*DataManager.get().getVoucherById(selectedVoucherId)?.let {

            VouchersDetailsViewAdapter().apply {
                var iconName: String = ""
                var title: String = ""
                var status: String = ""
                var statusColor: Int = -1
                var description: String = ""

                title = it.title
                status = it.price
                description = it.description
            }.also {
                vouchersDetailsView.fillViewWithData(it)
            }
        }
         */

        VouchersDetailsViewAdapter(
            iconName = "ic_fruit_basket",
            title = "Panier de légumes",
            status = "Vérification en cours",
            statusColor = ContextCompat.getColor(requireContext(), R.color.orange),
            description = "La taille et la composition du panier alimentaire sont adaptées aux préférences locales… Le panier comprend du riz au beurre, du poulet seekh kebab, de la tomate frite, du egg poch, du vert, du boissons gazeuses ordinaires, les boissons aux fruits, les boissons non alcoolisées."
        ).also {
            vouchersDetailsView?.fillViewWithData(it)
        }
    }

    /*voucherDetailsFragmentViewModel.generateQrCode(
    it.url,
    resources.displayMetrics.widthPixels
    )

     */

    private val vouchersDetailsViewCallback: VouchersDetailsView.VouchersDetailsViewCallback by lazy {
        object : VouchersDetailsView.VouchersDetailsViewCallback {
            override fun onBackButtonClicked() {
                dismissAllowingStateLoss()
            }

            override fun onUseVoucherButtonClicked() {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vouchersDetailsView = null
    }
}