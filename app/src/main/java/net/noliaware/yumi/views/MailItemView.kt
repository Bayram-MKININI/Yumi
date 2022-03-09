package net.noliaware.yumi.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import net.noliaware.yumi.R
import net.noliaware.yumi.utils.convertDpToPx
import net.noliaware.yumi.utils.layoutToTopLeft
import net.noliaware.yumi.utils.measureWrapContent

class MailItemView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var subjectTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var bodyTextView: TextView

    data class MailItemViewAdapter(
        val subject: String = "",
        val time: String = "",
        val body: String = ""
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        subjectTextView = findViewById(R.id.subject_text_view)
        timeTextView = findViewById(R.id.time_text_view)
        bodyTextView = findViewById(R.id.body_text_view)
    }

    fun fillViewWithData(mailItemViewAdapter: MailItemViewAdapter) {
        subjectTextView.text = mailItemViewAdapter.subject
        timeTextView.text = mailItemViewAdapter.time
        bodyTextView.text = mailItemViewAdapter.body
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        var viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        timeTextView.measureWrapContent()

        bodyTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth - convertDpToPx(48), MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        subjectTextView.measure(
            MeasureSpec.makeMeasureSpec(
                bodyTextView.measuredWidth - timeTextView.measuredWidth,
                MeasureSpec.AT_MOST
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        viewHeight =
            subjectTextView.measuredHeight + bodyTextView.measuredHeight + convertDpToPx(70)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        val marginLeft = (viewWidth - bodyTextView.measuredWidth) / 2

        subjectTextView.layoutToTopLeft(
            marginLeft,
            (viewHeight - (subjectTextView.measuredHeight + bodyTextView.measuredHeight + convertDpToPx(
                10
            ))) / 2
        )

        val timeTextViewRight = viewWidth - marginLeft
        val timeTextViewLeft = timeTextViewRight - timeTextView.measuredWidth
        val timeTextViewBottom = subjectTextView.bottom
        val timeTextViewTop = timeTextViewBottom - timeTextView.measuredHeight

        timeTextView.layout(
            timeTextViewLeft,
            timeTextViewTop,
            timeTextViewRight,
            timeTextViewBottom
        )

        bodyTextView.layoutToTopLeft(
            marginLeft,
            subjectTextView.bottom + convertDpToPx(10)
        )
    }
}