package net.noliaware.yumi.views

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import net.noliaware.yumi.R
import net.noliaware.yumi.model.GOLDEN_RATIO
import net.noliaware.yumi.utils.*
import kotlin.math.roundToInt

class VouchersDetailsView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var backView: View
    private lateinit var voucherImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var useVoucherTextView: TextView

    var callback: VouchersDetailsViewCallback? by weak()

    interface VouchersDetailsViewCallback {
        fun onBackButtonClicked()
        fun onUseVoucherButtonClicked()
    }

    data class VouchersDetailsViewAdapter(
        val iconName: String = "",
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

        backView = findViewById(R.id.back_view)
        backView.setOnClickListener(onButtonClickListener)

        voucherImageView = findViewById(R.id.voucher_image_view)
        titleTextView = findViewById(R.id.title_text_view)
        statusTextView = findViewById(R.id.status_text_view)
        descriptionTextView = findViewById(R.id.description_text_view)

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

    fun fillViewWithData(vouchersDetailsViewAdapter: VouchersDetailsViewAdapter) {
        voucherImageView.setImageResource(context.drawableIdByName(vouchersDetailsViewAdapter.iconName))
        titleTextView.text = vouchersDetailsViewAdapter.title
        statusTextView.text = vouchersDetailsViewAdapter.status
        statusTextView.setTextColor(vouchersDetailsViewAdapter.statusColor)
        descriptionTextView.text = vouchersDetailsViewAdapter.description
    }

    fun setVoucherBitmap(bitmap: Bitmap) {
        voucherImageView.setImageBitmap(bitmap)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        backView.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        val voucherImageViewSize = (viewWidth * (1 - 1 / GOLDEN_RATIO)).roundToInt()

        voucherImageView.measure(
            MeasureSpec.makeMeasureSpec(voucherImageViewSize, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(voucherImageViewSize, MeasureSpec.EXACTLY)
        )

        titleTextView.measureWrapContent()

        statusTextView.measureWrapContent()

        descriptionTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 9 / 10, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        useVoucherTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 7 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(40), MeasureSpec.EXACTLY)
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

        voucherImageView.layoutToTopLeft(
            (viewWidth - voucherImageView.measuredWidth) / 2,
            voucherImageView.measuredHeight * 75 / 100
        )

        titleTextView.layoutToTopLeft(
            (viewWidth - titleTextView.measuredWidth) / 2,
            voucherImageView.bottom + convertDpToPx(10)
        )

        statusTextView.layoutToTopLeft(
            (viewWidth - statusTextView.measuredWidth) / 2,
            titleTextView.bottom + convertDpToPx(10)
        )

        descriptionTextView.layoutToTopLeft(
            (viewWidth - descriptionTextView.measuredWidth) / 2,
            statusTextView.bottom + convertDpToPx(20)
        )

        useVoucherTextView.layoutToBottomLeft(
            (viewWidth - useVoucherTextView.measuredWidth) / 2,
            bottom - convertDpToPx(40)
        )
    }
}