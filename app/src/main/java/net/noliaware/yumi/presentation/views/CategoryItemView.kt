package net.noliaware.yumi.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.*

class CategoryItemView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var countTextView: TextView
    private lateinit var iconImageView: ImageView
    private lateinit var titleTextView: TextView

    data class CategoryItemViewAdapter(
        val count: Int = 0,
        val iconName: String = "",
        val title: String = ""
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        countTextView = findViewById(R.id.count_text_view)
        iconImageView = findViewById(R.id.icon_image_view)
        titleTextView = findViewById(R.id.title_text_view)
    }

    fun fillViewWithData(categoryItemViewAdapter: CategoryItemViewAdapter) {
        countTextView.text = categoryItemViewAdapter.count.toString()
        iconImageView.setImageResource(context.drawableIdByName(categoryItemViewAdapter.iconName))
        titleTextView.text = categoryItemViewAdapter.title
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        countTextView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(34), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(34), MeasureSpec.EXACTLY)
        )

        iconImageView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth / 3, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewWidth / 3, MeasureSpec.EXACTLY)
        )

        titleTextView.measureWrapContent()

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        countTextView.layoutToTopRight(
            viewWidth - convertDpToPx(10),
            convertDpToPx(10)
        )

        iconImageView.layoutToTopLeft(
            (viewWidth - iconImageView.measuredWidth) / 2,
            (viewHeight - iconImageView.measuredHeight) / 2
        )

        titleTextView.layoutToTopLeft(
            (viewHeight - titleTextView.measuredWidth) / 2,
            iconImageView.bottom + convertDpToPx(10)
        )
    }
}