package net.noliaware.yumi.feature_categories.presentation.views

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.presentation.adapters.BaseAdapter
import net.noliaware.yumi.commun.util.GRID
import net.noliaware.yumi.commun.util.MarginItemDecoration
import net.noliaware.yumi.commun.util.convertDpToPx
import net.noliaware.yumi.commun.util.inflate
import net.noliaware.yumi.commun.util.weak
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView.CategoryItemViewAdapter

class CategoriesListView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    private val categoryViewAdapters = mutableListOf<CategoryItemViewAdapter>()
    var callback: CategoriesListViewCallback? by weak()

    fun interface CategoriesListViewCallback {
        fun onCategoryClickedAtIndex(index: Int)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        layoutManager = GridLayoutManager(
            context,
            context.resources.getInteger(R.integer.number_of_columns_for_categories)
        )

        val spacing = convertDpToPx(10)

        setPadding(spacing, spacing, spacing, spacing)
        clipToPadding = false
        clipChildren = false
        addItemDecoration(MarginItemDecoration(spacing, GRID))

        BaseAdapter(categoryViewAdapters).apply {
            expressionViewHolderBinding = { eachItem, view ->
                (view as CategoryItemView).fillViewWithData(eachItem)
            }

            expressionOnCreateViewHolder = { viewGroup ->
                viewGroup.inflate(R.layout.category_item_layout)
            }

            onItemClicked = { position ->
                callback?.onCategoryClickedAtIndex(position)
            }

            adapter = this
        }
    }

    fun fillViewWithData(adapters: List<CategoryItemViewAdapter>) {
        if (categoryViewAdapters.isNotEmpty())
            categoryViewAdapters.clear()
        categoryViewAdapters.addAll(adapters)
        adapter?.notifyDataSetChanged()
    }
}