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

class UsedCategoriesView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var myVouchersTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private val categoryItemViewAdapters = mutableListOf<CategoryItemViewAdapter>()
    var callback: UsedCategoriesViewCallback? by weak()

    interface UsedCategoriesViewCallback {
        fun onCategoryClickedAtIndex(index: Int)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        myVouchersTextView = findViewById(R.id.my_vouchers_text_view)
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
                callback?.onCategoryClickedAtIndex(position)
            })
        }
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

        myVouchersTextView.measureWrapContent()

        val recyclerViewHeight = viewHeight - (myVouchersTextView.measuredHeight + convertDpToPx(30))
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

        myVouchersTextView.layoutToTopLeft(
            convertDpToPx(20),
            convertDpToPx(15)
        )

        recyclerView.layoutToTopLeft(
            0,
            myVouchersTextView.bottom + convertDpToPx(10)
        )
    }
}