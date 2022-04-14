package net.noliaware.yumi.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.adapters.BaseAdapter
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.presentation.views.AlertItemView.AlertItemViewAdapter

class AlertsView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var recyclerView: RecyclerView

    private val alertItemViewAdaptersList = mutableListOf<AlertItemViewAdapter>()

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {

        titleTextView = findViewById(R.id.title_text_view)
        descriptionTextView = findViewById(R.id.description_text_view)
        recyclerView = findViewById(R.id.recycler_view)

        val adapter = BaseAdapter(alertItemViewAdaptersList)

        adapter.expressionViewHolderBinding = { eachItem, view ->
            (view as AlertItemView).fillViewWithData(eachItem)
        }

        adapter.expressionOnCreateViewHolder = { viewGroup ->
            viewGroup.inflate(R.layout.alert_item_layout, false)
        }

        recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.addItemDecoration(MarginItemDecoration(convertDpToPx(16)))
            it.adapter = adapter
        }
    }

    fun fillViewWithData(adaptersList: List<AlertItemViewAdapter>) {

        if (alertItemViewAdaptersList.isNotEmpty())
            alertItemViewAdaptersList.clear()

        alertItemViewAdaptersList.addAll(adaptersList)
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

        val recyclerViewHeight =
            viewHeight - (titleTextView.measuredHeight + descriptionTextView.measuredHeight + getStatusBarHeight() + convertDpToPx(
                35
            ))

        recyclerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                recyclerViewHeight,
                MeasureSpec.EXACTLY
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