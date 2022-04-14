package net.noliaware.yumi.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.adapters.BaseAdapter
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.presentation.views.AccountItemView.AccountItemViewAdapter

class AccountsListView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val accountItemViewAdapters = mutableListOf<AccountItemViewAdapter>()
    var callback: AccountsListViewCallback? by weak()

    interface AccountsListViewCallback {
        fun onItemClickedAtIndex(index: Int)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {

        titleTextView = findViewById(R.id.title_text_view)
        descriptionTextView = findViewById(R.id.description_text_view)
        recyclerView = findViewById(R.id.recycler_view)
        progressBar = findViewById(R.id.progress_bar)

        val adapter = BaseAdapter(accountItemViewAdapters)

        adapter.expressionViewHolderBinding = { eachItem, view ->
            (view as AccountItemView).fillViewWithData(eachItem)
        }

        adapter.expressionOnCreateViewHolder = { viewGroup ->
            viewGroup.inflate(R.layout.account_item_layout, false)
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

    fun fillViewWithData(adaptersList: List<AccountItemViewAdapter>) {

        if (accountItemViewAdapters.isNotEmpty())
            accountItemViewAdapters.clear()

        accountItemViewAdapters.addAll(adaptersList)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun setProgressVisible(visible: Boolean) {
        if (visible) {
            progressBar.visibility = VISIBLE
            recyclerView.visibility = GONE
        } else {
            progressBar.visibility = GONE
            recyclerView.visibility = VISIBLE
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        titleTextView.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        descriptionTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 8 / 10, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        recyclerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                viewHeight - (titleTextView.measuredHeight + descriptionTextView.measuredHeight + getStatusBarHeight() + convertDpToPx(
                    35
                )),
                MeasureSpec.EXACTLY
            )
        )

        progressBar.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
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

        /*val progressBarLeft = (viewWidth - progressBar.measuredWidth) / 2
        val progressBarTop = (viewHeight - progressBar.measuredHeight) / 2

        progressBar.layoutToTopLeft(progressBarLeft, progressBarTop)

         */
    }
}