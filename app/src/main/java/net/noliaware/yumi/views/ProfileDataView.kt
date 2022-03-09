package net.noliaware.yumi.views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import net.noliaware.yumi.R
import net.noliaware.yumi.model.GOLDEN_RATIO
import net.noliaware.yumi.utils.convertDpToPx
import net.noliaware.yumi.utils.layoutToTopLeft
import net.noliaware.yumi.utils.layoutToTopRight
import net.noliaware.yumi.utils.measureWrapContent
import kotlin.math.roundToInt

class ProfileDataView(context: Context) : ViewGroup(context) {

    private lateinit var titleTextView: TextView
    private lateinit var valueTextView: TextView

    data class ProfileDataViewAdapter(
        val title: String = "",
        val value: String = ""
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    init {
        initView()
    }

    private fun initView() {

        LayoutInflater.from(context).also {
            it.inflate(R.layout.profile_data_layout, this, true)
        }

        titleTextView = findViewById(R.id.title_text_view)
        valueTextView = findViewById(R.id.value_text_view)
    }

    fun fillViewWithData(profileDataViewAdapter: ProfileDataViewAdapter) {
        titleTextView.text = profileDataViewAdapter.title
        valueTextView.text = profileDataViewAdapter.value
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        titleTextView.measureWrapContent()

        val valueTextViewWidth = (viewWidth / GOLDEN_RATIO).roundToInt()

        valueTextView.measure(
            MeasureSpec.makeMeasureSpec(valueTextViewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        val viewHeight =
            titleTextView.measuredHeight.coerceAtLeast(valueTextView.measuredHeight) + convertDpToPx(
                20
            )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        titleTextView.layoutToTopLeft(0, convertDpToPx(10))
        valueTextView.layoutToTopRight(viewWidth, titleTextView.top)
    }
}