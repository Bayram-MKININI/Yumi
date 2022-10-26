package net.noliaware.yumi.feature_categories.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.GOLDEN_RATIO
import net.noliaware.yumi.commun.presentation.views.DataValueView
import net.noliaware.yumi.commun.util.*
import kotlin.math.roundToInt

class VouchersDetailsView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var backView: View
    private lateinit var parentContentView: View
    private lateinit var contentView: LinearLayoutCompat
    private lateinit var displayVoucherTextView: TextView
    private lateinit var voucherStatusTextView: TextView

    var callback: VouchersDetailsViewCallback? by weak()

    interface VouchersDetailsViewCallback {
        fun onBackButtonClicked()
        fun onLocationClicked()
        fun onDisplayVoucherButtonClicked()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {

        backView = findViewById(R.id.back_view)
        backView.setOnClickListener(onButtonClickListener)

        parentContentView = findViewById(R.id.parent_content_layout)
        contentView = parentContentView.findViewById(R.id.content_layout)

        displayVoucherTextView = findViewById(R.id.display_voucher_text_view)
        displayVoucherTextView.setOnClickListener(onButtonClickListener)

        voucherStatusTextView = findViewById(R.id.voucher_status_text_view)
    }

    private val onButtonClickListener: OnClickListener by lazy {
        OnClickListener {
            when (it.id) {
                R.id.back_view -> callback?.onBackButtonClicked()
                R.id.display_voucher_text_view -> callback?.onDisplayVoucherButtonClicked()
            }
        }
    }

    private fun addSpace(spaceHeight: Int) {
        val space = Space(context)
        space.minimumHeight = convertDpToPx(spaceHeight)
        contentView.addView(space)
    }

    fun addImageByDrawableName(drawableName: String) {
        AppCompatImageView(context).apply {
            val imageViewSize = (width * (1 - 1 / GOLDEN_RATIO)).roundToInt()
            val params = LayoutParams(imageViewSize, imageViewSize)
            setImageResource(context.drawableIdByName(drawableName))
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        }.also {
            contentView.addView(it)
        }

        addSpace(20)
    }

    fun addTitle(title: String) {
        AppCompatTextView(context).apply {
            text = title
            textSize = 24f
            typeface = ResourcesCompat.getFont(context, R.font.sf_pro_display_semibold)
            setTextColor(ContextCompat.getColor(context, R.color.black_font))
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        }.also {
            contentView.addView(it)
        }

        addSpace(20)
    }

    fun addDataValue(dataValueViewAdapter: DataValueView.DataValueViewAdapter) {
        DataValueView(context).also {
            //layoutParams = LayoutParams(measuredWidth * 9 / 10, LayoutParams.WRAP_CONTENT)
            contentView.addView(it)
            it.fillViewWithData(dataValueViewAdapter)
        }
        addSpace(10)
    }

    fun addLocationView(onLocationClicked: () -> Unit) {
        LocationView(context).apply {
            locationClickedAction = onLocationClicked
        }.also {
            contentView.addView(it)
        }

        addSpace(20)
    }

    fun setVoucherStatus(voucherStatus: String) {
        voucherStatusTextView.isVisible = true
        displayVoucherTextView.isGone = true
        voucherStatusTextView.text = voucherStatus
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        backView.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        displayVoucherTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 7 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(40), MeasureSpec.EXACTLY)
        )

        if (voucherStatusTextView.isVisible) {
            voucherStatusTextView.measureWrapContent()
        }

        displayVoucherTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 7 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(40), MeasureSpec.EXACTLY)
        )

        val parentContentViewHeight =
            viewHeight - (backView.measuredHeight + displayVoucherTextView.measuredHeight + convertDpToPx(
                100
            ))

        parentContentView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(parentContentViewHeight, MeasureSpec.EXACTLY)
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

        parentContentView.layoutToTopLeft(
            0,
            backView.bottom + convertDpToPx(20)
        )

        if (voucherStatusTextView.isVisible) {
            voucherStatusTextView.layoutToBottomLeft(
                (viewWidth - voucherStatusTextView.measuredWidth) / 2,
                bottom - convertDpToPx(40)
            )
        } else {
            displayVoucherTextView.layoutToBottomLeft(
                (viewWidth - displayVoucherTextView.measuredWidth) / 2,
                bottom - convertDpToPx(40)
            )
        }
    }
}