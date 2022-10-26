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

        CategoriesView.CategoriesViewAdapter(
            description = getString(R.string.categories_list),
            categoryItemViewAdapters = itemViewAdapters ?: listOf()
        ).apply {
            categoriesView?.fillViewWithData(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoriesView = null
    }
}