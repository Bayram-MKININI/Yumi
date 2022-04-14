package net.noliaware.yumi.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.adapters.BaseAdapter
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.presentation.views.CategoryItemView.CategoryItemViewAdapter

class CategoriesView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private val categoryItemViewAdapters = mutableListOf<CategoryItemViewAdapter>()
    var callback: CategoriesViewCallback? by weak()

    data class CategoriesViewAdapter(
        val description: String,
        val categoryItemViewAdapters: List<CategoryItemViewAdapter>
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    interface CategoriesViewCallback {
        fun onItemClickedAtIndex(index: Int)
    }

    private fun initView() {

        titleTextView = findViewById(R.id.title_text_view)
        descriptionTextView = findViewById(R.id.description_text_view)
        recyclerView = findViewById(R.id.recycler_view)

        val adapter = BaseAdapter(categoryItemViewAdapters)

        adapter.expressionViewHolderBinding = { eachItem, view ->
            (view as CategoryItemView).fillViewWithData(eachItem)
        }

        adapter.expressionOnCreateViewHolder = { viewGroup ->
            viewGroup.inflate(R.layout.category_item_layout, false)
        }

        recyclerView.also {

            it.layoutManager = GridLayoutManager(
                context,
                context.resources.getInteger(R.integer.number_of_columns_for_categories)
            )

            val spacing = convertDpToPx(10)

            it.setPadding(spacing, spacing, spacing, spacing)
            it.clipToPadding = false
            it.clipChildren = false
            it.addItemDecoration(MarginItemDecoration(spacing, GRID))

            it.adapter = adapter

            it.onItemClicked(onClick = { position, _ ->
                callback?.onItemClickedAtIndex(position)
            })
        }
    }

    fun fillViewWithData(categoriesViewAdapter: CategoriesViewAdapter) {
        descriptionTextView.text = categoriesViewAdapter.description

        if (categoryItemViewAdapters.isNotEmpty())
            categoryItemViewAdapters.clear()

        categoryItemViewAdapters.addAll(categoriesViewAdapter.categoryItemViewAdapters)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        titleTextView.measureWrapContent()

        descriptionTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 8 / 10, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        recyclerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                viewHeight - (titleTextView.measuredHeight + descriptionTextView.measuredHeight + getStatusBarHeight() + convertDpToPx(
                    35
                )), MeasureSpec.EXACTLY
            )
        )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        titleTextView.layoutToTopLeft(
            (viewWidth - titleTextView.measuredWidth) / 2,
            getStatusBarHeight() + convertDpToPx(15)
        )

        descriptionTextView.layoutToTopLeft(
            (viewWidth - descriptionTextView.measuredWidth) / 2,
            titleTextView.bottom + convertDpToPx(10)
        )

        recyclerView.layoutToBottomLeft(0, viewHeight)
    }
}