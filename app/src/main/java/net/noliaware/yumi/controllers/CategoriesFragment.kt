package net.noliaware.yumi.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import net.noliaware.yumi.R
import net.noliaware.yumi.model.VOUCHERS_LIST_FRAGMENT_TAG
import net.noliaware.yumi.utils.handleErrorResponse
import net.noliaware.yumi.utils.inflate
import net.noliaware.yumi.views.CategoriesView
import net.noliaware.yumi.views.CategoriesView.CategoriesViewCallback
import net.noliaware.yumi.views.CategoryItemView.CategoryItemViewAdapter

class CategoriesFragment : Fragment() {

    private var categoriesView: CategoriesView? = null
    private lateinit var categoriesFragmentViewModel: CategoriesFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.categories_layout, false)?.apply {
            categoriesView = this as CategoriesView
            categoriesView?.callback = categoriesViewCallback
        }
    }

    private val categoriesViewCallback: CategoriesViewCallback by lazy {
        object : CategoriesViewCallback {
            override fun onItemClickedAtIndex(index: Int) {
                val fragmentTransaction = childFragmentManager.beginTransaction()
                //val vouchersListFragment = VouchersListFragment.newInstance(DataManager.get().categoriesList[index].categoryId)
                val vouchersListFragment = VouchersListFragment.newInstance("Test")
                vouchersListFragment.show(fragmentTransaction, VOUCHERS_LIST_FRAGMENT_TAG)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesFragmentViewModel =
            ViewModelProvider(requireActivity())[CategoriesFragmentViewModel::class.java]
        setUpEvents()
        bindViewToData()
        categoriesFragmentViewModel.callGetAlertsWebservice()
    }

    private fun setUpEvents() {
        categoriesFragmentViewModel.liveAlertsList.observe(
            viewLifecycleOwner
        ) { alertsList ->
            //refreshAdapters(messagesInboxList)
        }

        categoriesFragmentViewModel.errorInboxResponseLiveData.observe(
            viewLifecycleOwner
        ) { errorResponse ->
            activity?.handleErrorResponse(errorResponse)
        }
    }

    private fun bindViewToData() {

        val categoriesViewAdapter =
            CategoriesView.CategoriesViewAdapter(description = "Le Lorem est simplement du faux texte")

        /*DataManager.get().categoriesList.forEach { category ->

            CategoryItemViewAdapter().apply {
                count = category.vouchersList.size
                iconName = category.categoryName
                iconName = ""
            }.also {
                categoryItemViewAdapter.categoryItemViewAdapters.add(it)
            }
        }*/

        CategoryItemViewAdapter(
            count = 12,
            iconName = "ic_food",
            title = "Alimentaire"
        ).also {
            categoriesViewAdapter.categoryItemViewAdapters.add(it)
        }

        CategoryItemViewAdapter(
            count = 3,
            iconName = "ic_sports",
            title = "Sports"
        ).also {
            categoriesViewAdapter.categoryItemViewAdapters.add(it)
        }

        CategoryItemViewAdapter(
            count = 6,
            iconName = "ic_health",
            title = "Santé"
        ).also {
            categoriesViewAdapter.categoryItemViewAdapters.add(it)
        }

        CategoryItemViewAdapter(
            count = 15,
            iconName = "ic_services",
            title = "Services"
        ).also {
            categoriesViewAdapter.categoryItemViewAdapters.add(it)
        }

        CategoryItemViewAdapter(
            count = 2,
            iconName = "ic_energy",
            title = "Energie"
        ).also {
            categoriesViewAdapter.categoryItemViewAdapters.add(it)
        }

        categoriesView?.fillViewWithData(categoriesViewAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoriesView = null
    }
}