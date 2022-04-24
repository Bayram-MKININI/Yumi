package net.noliaware.yumi.feature_categories.presentation.views

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.GOLDEN_RATIO
import net.noliaware.yumi.commun.util.convertDpToPx
import net.noliaware.yumi.commun.util.getStatusBarHeight
import net.noliaware.yumi.commun.util.layoutToTopLeft
import net.noliaware.yumi.commun.util.weak
import kotlin.math.roundToInt

class QrCodeView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var backView: View
    private lateinit var qrCodeImageView: ImageView

    var callback: QrCodeViewCallback? by weak()

    interface QrCodeViewCallback {
        fun onBackButtonClicked()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        backView = findViewById(R.id.back_view)
        backView.setOnClickListener(onButtonClickListener)
        qrCodeImageView = findViewById(R.id.qr_code_image_view)
    }

    private val onButtonClickListener: OnClickListener by lazy {
        OnClickListener {
            if (it.id == R.id.back_view)
                callback?.onBackButtonClicked()
        }
    }

    fun setQrCode(bitmap: Bitmap) {
        qrCodeImageView.setImageBitmap(bitmap)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        backView.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        val qrCodeImageViewSize = (viewWidth / GOLDEN_RATIO).roundToInt()

        qrCodeImageView.measure(
            MeasureSpec.makeMeasureSpec(qrCodeImageViewSize, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(qrCodeImageViewSize, MeasureSpec.EXACTLY)
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

        qrCodeImageView.layoutToTopLeft(
            (viewWidth - qrCodeImageView.measuredWidth) / 2,
            (viewHeight - qrCodeImageView.measuredHeight) / 2
        )
    }
}