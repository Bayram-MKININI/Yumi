package net.noliaware.yumi.feature_categories.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.VOUCHERS_LIST_FRAGMENT_TAG
import net.noliaware.yumi.commun.util.inflate
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesView
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesView.CategoriesViewAdapter
import net.noliaware.yumi.feature_categories.presentation.views.CategoriesView.CategoriesViewCallback
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView.CategoryItemViewAdapter

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    companion object {
        fun newInstance(): CategoriesFragment = CategoriesFragment()
    }

    private var categoriesView: CategoriesView? = null
    private val viewModel by activityViewModels<HomeFragmentViewModel>()

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

                viewModel.accountData?.categories?.get(index)?.let { category ->
                    VouchersListFragment.newInstance(category.categoryId, category.categoryLabel)
                        .show(
                            childFragmentManager.beginTransaction(),
                            VOUCHERS_LIST_FRAGMENT_TAG
                        )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewToData()
    }

    private fun bindViewToData() {

        val itemViewAdapters = viewModel.accountData?.categories?.map { category ->
            CategoryItemViewAdapter(
                count = category.voucherCount,
                iconName = category.categoryIcon ?: "ic_food",
                title = category.categoryLabel
            )
        }

        itemViewAdapters?.let {
            CategoriesViewAdapter(
                description = "Le Lorem est simplement du faux texte",
                categoryItemViewAdapters = it
            ).apply {
                categoriesView?.fillViewWithData(this)
            }
        }

        /*CategoryItemViewAdapter(
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

         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoriesView = null
    }
}