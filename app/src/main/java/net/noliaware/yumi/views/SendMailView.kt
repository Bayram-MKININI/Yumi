package net.noliaware.yumi.views

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import net.noliaware.yumi.R
import net.noliaware.yumi.utils.*

class SendMailView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var backView: View
    private lateinit var titleTextView: TextView
    private lateinit var backgroundView: View
    private lateinit var subjectEditText: EditText
    private lateinit var separatorLineView: View
    private lateinit var nestedScrollView: View
    private lateinit var mailEditText: EditText
    private lateinit var sendButton: View
    private val visibleRect = Rect()

    var callback: SendMailViewCallback? by weak()

    interface SendMailViewCallback {
        fun onBackButtonClicked()
        fun onClearButtonClicked()
        fun onSendMailClicked(subject: String, text: String)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {

        backView = findViewById(R.id.back_view)
        backView.setOnClickListener(buttonClickListener)

        titleTextView = findViewById(R.id.title_text_view)

        backgroundView = findViewById(R.id.background_view)
        subjectEditText = findViewById(R.id.subject_edit_text)
        separatorLineView = findViewById(R.id.separator_line_view)

        nestedScrollView = findViewById(R.id.nested_scroll_view)
        mailEditText = nestedScrollView.findViewById(R.id.mail_edit_text)

        sendButton = findViewById(R.id.send_fab)
        sendButton.setOnClickListener(buttonClickListener)
    }

    private val buttonClickListener: OnClickListener by lazy {
        OnClickListener {
            when (it.id) {
                R.id.back_view -> {
                    callback?.onBackButtonClicked()
                }
                R.id.send_fab -> {
                    callback?.onSendMailClicked(
                        mailEditText.text.toString(),
                        mailEditText.text.toString()
                    )
                }
            }
        }
    }

    fun computeMailView() {
        post {
            mailEditText.requestLayout()
        }
    }

    fun clearMail() {
        mailEditText.text.clear()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        backView.measureWrapContent()

        titleTextView.measureWrapContent()

        getWindowVisibleDisplayFrame(visibleRect)

        val backgroundViewHeight =
            viewHeight - (backView.measuredHeight + titleTextView.measuredHeight + getStatusBarHeight() + convertDpToPx(
                40
            ) + (viewHeight - visibleRect.height()))

        backgroundView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth - convertDpToPx(40), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(backgroundViewHeight, MeasureSpec.EXACTLY)
        )

        subjectEditText.measure(
            MeasureSpec.makeMeasureSpec(
                backgroundView.measuredWidth - convertDpToPx(30),
                MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        separatorLineView.measure(
            MeasureSpec.makeMeasureSpec(subjectEditText.measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(1), MeasureSpec.EXACTLY)
        )

        val availableHeightForBody =
            backgroundView.measuredHeight - (subjectEditText.measuredHeight + separatorLineView.measuredHeight + convertDpToPx(
                50
            ))

        nestedScrollView.measure(
            MeasureSpec.makeMeasureSpec(subjectEditText.measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(availableHeightForBody, MeasureSpec.EXACTLY)
        )

        mailEditText.measure(
            MeasureSpec.makeMeasureSpec(nestedScrollView.measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        mailEditText.measure(
            MeasureSpec.makeMeasureSpec(nestedScrollView.measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                availableHeightForBody.coerceAtLeast(mailEditText.measuredHeight),
                MeasureSpec.EXACTLY
            )
        )

        sendButton.measureWrapContent()

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        val viewWidth = right - left
        val viewHeight = bottom - top

        backView.layoutToTopLeft(
            convertDpToPx(10),
            getStatusBarHeight() + convertDpToPx(10)
        )

        titleTextView.layoutToTopLeft(
            (viewWidth - titleTextView.measuredWidth) / 2,
            backView.bottom + convertDpToPx(10)
        )

        backgroundView.layoutToTopLeft(
            (viewWidth - backgroundView.measuredWidth) / 2,
            titleTextView.bottom + convertDpToPx(20)
        )

        subjectEditText.layoutToTopLeft(
            (viewWidth - subjectEditText.measuredWidth) / 2,
            backgroundView.top + convertDpToPx(20)
        )

        separatorLineView.layoutToTopLeft(
            (viewWidth - separatorLineView.measuredWidth) / 2,
            subjectEditText.bottom + convertDpToPx(10)
        )

        nestedScrollView.layoutToTopLeft(
            (viewWidth - mailEditText.measuredWidth) / 2,
            separatorLineView.bottom
        )

        sendButton.layoutToBottomRight(
            backgroundView.right,
            backgroundView.bottom
        )
    }
}