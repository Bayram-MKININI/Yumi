package net.noliaware.yumi.feature_categories.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.presentation.adapters.BaseAdapter
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView.CategoryItemViewAdapter

class CategoriesView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var headerView: View
    private lateinit var helloTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var voucherImageView: ImageView
    private lateinit var voucherBadgeTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private val categoryItemViewAdapters = mutableListOf<CategoryItemViewAdapter>()
    var callback: CategoriesViewCallback? by weak()

    data class CategoriesViewAdapter(
        val categoryItemViewAdapters: List<CategoryItemViewAdapter>
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    fun interface CategoriesViewCallback {
        fun onItemClickedAtIndex(index: Int)
    }

    private fun initView() {

        headerView = findViewById(R.id.header_view)
        helloTextView = findViewById(R.id.hello_text_view)
        nameTextView = findViewById(R.id.name_text_view)
        voucherImageView = findViewById(R.id.voucher_image_view)
        voucherBadgeTextView = findViewById(R.id.voucher_badge_text_view)
        titleTextView = findViewById(R.id.title_text_view)
        recyclerView = findViewById(R.id.recycler_view)

        recyclerView.also {

            it.layoutManager = GridLayoutManager(
                context, context.resources.getInteger(R.integer.number_of_columns_for_categories)
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
                    callback?.onItemClickedAtIndex(position)
                }
                it.adapter = this
            }
        }
    }

    fun setUserData(
        helloText: String,
        userName: String,
        voucherBadgeValue: String?
    ) {
        helloTextView.text = helloText
        nameTextView.text = userName
        voucherBadgeValue?.let {
            voucherBadgeTextView.isVisible = true
            voucherBadgeTextView.text = voucherBadgeValue
        }
    }

    fun fillViewWithData(categoriesViewAdapter: CategoriesViewAdapter) {
        refreshCategoryList(categoriesViewAdapter.categoryItemViewAdapters)
    }

    fun refreshCategoryList(categoryItemViewAdapters: List<CategoryItemViewAdapter>) {
        if (this.categoryItemViewAdapters.isNotEmpty()) {
            this.categoryItemViewAdapters.clear()
        }
        this.categoryItemViewAdapters.addAll(categoryItemViewAdapters)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        headerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                getStatusBarHeight() + convertDpToPx(111), MeasureSpec.EXACTLY
            )
        )

        helloTextView.measureWrapContent()
        nameTextView.measureWrapContent()

        voucherImageView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY)
        )

        voucherBadgeTextView.measureWrapContent()
        titleTextView.measureWrapContent()

        val recyclerViewHeight =
            viewHeight - (headerView.measuredHeight + titleTextView.measuredHeight +
                    convertDpToPx(40))

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

        headerView.layoutToTopLeft(0, 0)

        val contentHeight = helloTextView.measuredHeight + nameTextView.measuredHeight
        val headerHeight = headerView.measuredHeight - getStatusBarHeight()
        helloTextView.layoutToTopLeft(
            convertDpToPx(20),
            getStatusBarHeight() + (headerHeight - contentHeight) / 2
        )

        nameTextView.layoutToTopLeft(
            helloTextView.left, helloTextView.bottom
        )

        voucherImageView.layoutToTopRight(
            right - convertDpToPx(30),
            helloTextView.top + convertDpToPx(8)
        )

        voucherBadgeTextView.layoutToTopLeft(
            voucherImageView.right - voucherImageView.measuredWidth * 3 / 10,
            voucherImageView.top
        )

        titleTextView.layoutToTopLeft(
            convertDpToPx(15),
            headerView.bottom + convertDpToPx(15)
        )

        recyclerView.layoutToTopLeft(
            0,
            titleTextView.bottom + convertDpToPx(5)
        )
    }
}