package net.noliaware.yumi.feature_categories.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.GOLDEN_RATIO
import net.noliaware.yumi.commun.presentation.views.DataValueView
import net.noliaware.yumi.commun.util.*
import kotlin.math.roundToInt


class VouchersDetailsView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var backView: View
    private lateinit var parentContentView: View
    private lateinit var contentView: LinearLayoutCompat
    private lateinit var useVoucherTextView: TextView

    var callback: VouchersDetailsViewCallback? by weak()

    interface VouchersDetailsViewCallback {
        fun onBackButtonClicked()
        fun onUseVoucherButtonClicked()
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

        useVoucherTextView = findViewById(R.id.use_vouchers_text_view)
        useVoucherTextView.setOnClickListener(onButtonClickListener)
    }

    private val onButtonClickListener: OnClickListener by lazy {
        OnClickListener {
            when (it.id) {
                R.id.back_view -> callback?.onBackButtonClicked()
                R.id.use_vouchers_text_view -> callback?.onUseVoucherButtonClicked()
            }
        }
    }

    private fun addSpace(spaceHeight: Int) {
        val space = Space(context)
        space.minimumHeight = convertDpToPx(spaceHeight)
        contentView.addView(space)
    }

    fun addImageByDrawableName(drawableName: String) {
        post {
            AppCompatImageView(context).apply {
                val imageViewSize = (measuredWidth * (1 - 1 / GOLDEN_RATIO)).roundToInt()
                val params = LinearLayout.LayoutParams(imageViewSize, imageViewSize)
                setImageResource(context.drawableIdByName(drawableName))
                layoutParams = params
            }.also {
                contentView.addView(it)
            }

            addSpace(20)
        }
    }

    fun addTitle(title: String) {
        post {
            AppCompatTextView(context).apply {
                val params =
                    LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                text = title
                textSize = 24f
                typeface = ResourcesCompat.getFont(context, R.font.sf_pro_display_semibold)
                setTextColor(ContextCompat.getColor(context, R.color.black_font))
                layoutParams = params
            }.also {
                contentView.addView(it)
            }

            addSpace(20)
        }
    }

    fun addText(textStr: String) {
        post {
            AppCompatTextView(context).apply {
                val params =
                    LinearLayout.LayoutParams(measuredWidth * 9 / 10, LayoutParams.WRAP_CONTENT)
                text = textStr
                typeface = ResourcesCompat.getFont(context, R.font.sf_pro_text_regular)
                setTextColor(ContextCompat.getColor(context, R.color.black_font))
                layoutParams = params
            }.also {
                contentView.addView(it)
            }

            addSpace(10)
        }
    }

    fun addDataValue(dataValueViewAdapter: DataValueView.DataValueViewAdapter) {
        post {
            DataValueView(context).also {
                val params = LayoutParams(measuredWidth * 9 / 10, LayoutParams.WRAP_CONTENT)
                it.fillViewWithData(dataValueViewAdapter)
                //layoutParams = params
                contentView.addView(it)
            }
            addSpace(10)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        backView.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        useVoucherTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 7 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(40), MeasureSpec.EXACTLY)
        )

        val parentContentViewHeight =
            viewHeight - (backView.measuredHeight + useVoucherTextView.measuredHeight + convertDpToPx(
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

        useVoucherTextView.layoutToBottomLeft(
            (viewWidth - useVoucherTextView.measuredWidth) / 2,
            bottom - convertDpToPx(40)
        )
    }
}