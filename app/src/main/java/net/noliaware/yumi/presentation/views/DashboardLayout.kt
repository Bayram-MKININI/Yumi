package net.noliaware.yumi.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.convertDpToPx
import net.noliaware.yumi.commun.util.layoutToTopLeft


class DashboardLayout : ViewGroup {

    constructor(context: Context?) : super(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        var viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        val columnCount = context.resources.getInteger(R.integer.number_of_columns_for_categories)

        val allSpace = ((columnCount - 1) * convertDpToPx(20))

        val childWidth = (viewWidth - allSpace) / columnCount

        children.forEach {
            it.measure(
                MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
        }

        val rowCount = (childCount - 1) / columnCount + 1

        viewHeight = rowCount * getChildAt(0).measuredHeight + ((rowCount + 1) * convertDpToPx(20))

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        val space = convertDpToPx(20)

        val cols = context.resources.getInteger(R.integer.number_of_columns_for_categories)

        children.forEachIndexed { index, child ->

            val row = index / cols
            val col = index % cols

            val childLeft = child.measuredWidth * col + if (col > 0) space * col else 0
            val childTop = child.measuredHeight * row + if (row > 0) space * row else 0

            child.layoutToTopLeft(childLeft, childTop)
        }
    }
}