package net.noliaware.yumi.feature_alerts.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.convertDpToPx
import net.noliaware.yumi.commun.util.layoutToTopLeft
import net.noliaware.yumi.commun.util.measureWrapContent
import net.noliaware.yumi.feature_alerts.domain.model.AlertPriority

class AlertItemView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var backgroundView: View
    private lateinit var priorityImageView: ImageView
    private lateinit var senderTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var bodyTextView: TextView

    data class AlertItemViewAdapter(
        val priority: AlertPriority = AlertPriority.NONE,
        val sender: String = "",
        val time: String = "",
        val body: String = ""
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        backgroundView = findViewById(R.id.background_view)
        priorityImageView = findViewById(R.id.priority_image_view)
        senderTextView = findViewById(R.id.sender_text_view)
        timeTextView = findViewById(R.id.time_text_view)
        bodyTextView = findViewById(R.id.body_text_view)
    }

    fun fillViewWithData(alertItemViewAdapter: AlertItemViewAdapter) {

        senderTextView.text = alertItemViewAdapter.sender
        timeTextView.text = alertItemViewAdapter.time
        bodyTextView.text = alertItemViewAdapter.body

        when (alertItemViewAdapter.priority) {

            AlertPriority.RED -> {
                priorityImageView.visibility = VISIBLE
                priorityImageView.setBackgroundResource(R.drawable.ring_filled_red)
                priorityImageView.setImageResource(R.drawable.ic_danger)
            }

            AlertPriority.ORANGE -> {
                priorityImageView.visibility = VISIBLE
                priorityImageView.setBackgroundResource(R.drawable.ring_filled_orange)
                priorityImageView.setImageResource(R.drawable.ic_warning)
            }

            else -> priorityImageView.visibility = GONE
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        var viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        priorityImageView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(32), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(32), MeasureSpec.EXACTLY)
        )

        timeTextView.measureWrapContent()

        val bodyTextViewWidth = viewWidth - (priorityImageView.measuredWidth * 2 - convertDpToPx(8))

        bodyTextView.measure(
            MeasureSpec.makeMeasureSpec(bodyTextViewWidth, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        senderTextView.measure(
            MeasureSpec.makeMeasureSpec(
                bodyTextViewWidth - timeTextView.measuredWidth,
                MeasureSpec.AT_MOST
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        viewHeight =
            priorityImageView.measuredHeight + senderTextView.measuredHeight + bodyTextView.measuredHeight + convertDpToPx(
                40
            )

        backgroundView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth - convertDpToPx(8), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight - convertDpToPx(5), MeasureSpec.EXACTLY)
        )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        if (priorityImageView.visibility == VISIBLE)
            priorityImageView.layoutToTopLeft(0, 0)

        backgroundView.layoutToTopLeft(convertDpToPx(4), convertDpToPx(4))

        val edgeSpace = priorityImageView.measuredWidth - convertDpToPx(4)

        var senderTextViewTop = priorityImageView.bottom - convertDpToPx(4)

        if (priorityImageView.visibility == GONE)
            senderTextViewTop = edgeSpace

        var senderTextViewLeft = priorityImageView.right - convertDpToPx(4)

        if (priorityImageView.visibility == GONE)
            senderTextViewLeft = edgeSpace

        senderTextView.layoutToTopLeft(
            senderTextViewTop,
            senderTextViewLeft
        )

        val timeTextViewRight = backgroundView.right - (senderTextView.left - backgroundView.left)
        val timeTextViewLeft = timeTextViewRight - timeTextView.measuredWidth
        val timeTextViewBottom = senderTextView.bottom
        val timeTextViewTop = timeTextViewBottom - timeTextView.measuredHeight

        timeTextView.layout(
            timeTextViewLeft,
            timeTextViewTop,
            timeTextViewRight,
            timeTextViewBottom
        )

        bodyTextView.layoutToTopLeft(
            backgroundView.left + (backgroundView.measuredWidth - bodyTextView.measuredWidth) / 2,
            senderTextView.bottom + convertDpToPx(10)
        )
    }
}