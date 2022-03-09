package net.noliaware.yumi.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import net.noliaware.yumi.R
import net.noliaware.yumi.utils.convertDpToPx
import net.noliaware.yumi.utils.layoutToTopLeft
import net.noliaware.yumi.utils.measureWrapContent

class VoucherItemView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var titleTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var descriptionTextView: TextView

    data class VoucherItemViewAdapter(
        val title: String = "",
        val status: String = "",
        val statusColor: Int = -1,
        val description: String = ""
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        titleTextView = findViewById(R.id.title_text_view)
        statusTextView = findViewById(R.id.status_text_view)
        descriptionTextView = findViewById(R.id.description_text_view)
    }

    fun fillViewWithData(voucherItemViewAdapter: VoucherItemViewAdapter) {
        titleTextView.text = voucherItemViewAdapter.title
        statusTextView.text = voucherItemViewAdapter.status
        statusTextView.setTextColor(voucherItemViewAdapter.statusColor)
        descriptionTextView.text = voucherItemViewAdapter.description
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        var viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        titleTextView.measureWrapContent()

        statusTextView.measureWrapContent()

        descriptionTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 9 / 10, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        viewHeight =
            titleTextView.measuredHeight + statusTextView.measuredHeight + descriptionTextView.measuredHeight + convertDpToPx(
                40
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
            convertDpToPx(15),
            convertDpToPx(15)
        )

        statusTextView.layoutToTopLeft(
            convertDpToPx(15),
            titleTextView.bottom + convertDpToPx(5)
        )

        descriptionTextView.layoutToTopLeft(
            convertDpToPx(15),
            statusTextView.bottom + convertDpToPx(5)
        )
    }
}