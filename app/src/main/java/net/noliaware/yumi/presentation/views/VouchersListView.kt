package net.noliaware.yumi.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.adapters.BaseAdapter
import net.noliaware.yumi.commun.util.MarginItemDecoration
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.presentation.views.VoucherItemView.VoucherItemViewAdapter

class VouchersListView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var backView: View
    private lateinit var titleTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private val voucherItemViewAdaptersList = mutableListOf<VoucherItemViewAdapter>()
    var callback: VouchersListViewCallback? by weak()

    interface VouchersListViewCallback {
        fun onBackButtonClicked()
        fun onItemClickedAtIndex(index: Int)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {

        backView = findViewById(R.id.back_view)
        backView.setOnClickListener {
            callback?.onBackButtonClicked()
        }

        titleTextView = findViewById(R.id.title_text_view)
        recyclerView = findViewById(R.id.recycler_view)

        val adapter = BaseAdapter(voucherItemViewAdaptersList)

        adapter.expressionViewHolderBinding = { eachItem, view ->
            (view as VoucherItemView).fillViewWithData(eachItem)
        }

        adapter.expressionOnCreateViewHolder = { viewGroup ->
            viewGroup.inflate(R.layout.voucher_item_layout, false)
        }

        recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.addItemDecoration(MarginItemDecoration(convertDpToPx(20)))
            it.adapter = adapter
            it.onItemClicked(onClick = { position, _ ->
                callback?.onItemClickedAtIndex(position)
            })
        }
    }

    fun fillViewWithData(adaptersList: List<VoucherItemViewAdapter>) {

        if (voucherItemViewAdaptersList.isNotEmpty())
            voucherItemViewAdaptersList.clear()

        voucherItemViewAdaptersList.addAll(adaptersList)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        backView.measureWrapContent()

        titleTextView.measureWrapContent()

        val recyclerViewHeight =
            viewHeight - (backView.measuredHeight + titleTextView.measuredHeight + getStatusBarHeight() + convertDpToPx(
                35
            ))

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

        backView.layoutToTopLeft(convertDpToPx(10), getStatusBarHeight() + convertDpToPx(10))

        titleTextView.layoutToTopLeft(
            (viewWidth - titleTextView.measuredWidth) / 2,
            backView.bottom + convertDpToPx(10)
        )

        recyclerView.layoutToTopLeft(0, titleTextView.bottom + convertDpToPx(10))
    }
}