package net.noliaware.yumi.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.presentation.adapters.BaseAdapter
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView.CategoryItemViewAdapter

class ProfileCategoriesView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var titleTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private val categoryItemViewAdapters = mutableListOf<CategoryItemViewAdapter>()
    var callback: ProfileCategoriesViewCallback? by weak()

    fun interface ProfileCategoriesViewCallback {
        fun onCategoryClickedAtIndex(index: Int)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        titleTextView = findViewById(R.id.title_text_view)
        recyclerView = findViewById(R.id.recycler_view)

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

            BaseAdapter(categoryItemViewAdapters).apply {
                expressionViewHolderBinding = { eachItem, view ->
                    (view as CategoryItemView).fillViewWithData(eachItem)
                }
                expressionOnCreateViewHolder = { viewGroup ->
                    viewGroup.inflate(R.layout.category_item_layout, false)
                }
                onItemClicked = { position ->
                    callback?.onCategoryClickedAtIndex(position)
                }
                it.adapter = this
            }
        }
    }

    fun setViewTitle(title: String) {
        titleTextView.text = title
    }

    fun fillViewWithData(adapters: List<CategoryItemViewAdapter>) {
        if (categoryItemViewAdapters.isNotEmpty())
            categoryItemViewAdapters.clear()
        categoryItemViewAdapters.addAll(adapters)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        titleTextView.measureWrapContent()

        val recyclerViewHeight =
            viewHeight - (titleTextView.measuredHeight + convertDpToPx(30))
        recyclerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(recyclerViewHeight, MeasureSpec.EXACTLY)
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
            convertDpToPx(20),
            convertDpToPx(15)
        )

        recyclerView.layoutToTopLeft(
            0,
            titleTextView.bottom + convertDpToPx(10)
        )
    }
}