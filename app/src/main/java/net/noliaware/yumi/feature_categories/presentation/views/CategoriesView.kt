package net.noliaware.yumi.feature_categories.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.presentation.views.ClipartTabView
import net.noliaware.yumi.commun.util.convertDpToPx
import net.noliaware.yumi.commun.util.getStatusBarHeight
import net.noliaware.yumi.commun.util.layoutToTopLeft
import net.noliaware.yumi.commun.util.measureWrapContent
import net.noliaware.yumi.commun.util.removeOverScroll

class CategoriesView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var headerView: View
    private lateinit var helloTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var youHaveTextView: TextView
    private lateinit var voucherBadgeTextView: TextView
    private lateinit var vouchersTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var availableTabView: ClipartTabView
    private lateinit var usedTabView: ClipartTabView
    private lateinit var cancelledTabView: ClipartTabView
    private lateinit var contentView: View
    private lateinit var viewPager: ViewPager2

    val getViewPager get() = viewPager

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        headerView = findViewById(R.id.header_view)
        helloTextView = findViewById(R.id.hello_text_view)
        nameTextView = findViewById(R.id.name_text_view)
        youHaveTextView = findViewById(R.id.you_have_text_view)
        voucherBadgeTextView = findViewById(R.id.voucher_badge_text_view)
        vouchersTextView = findViewById(R.id.vouchers_text_view)
        titleTextView = findViewById(R.id.title_text_view)
        availableTabView = findViewById(R.id.available_tab_layout)
        availableTabView.setTitle(context.getString(R.string.available).uppercase())
        availableTabView.setOnClickListener {
            setFirstTabSelected()
            viewPager.setCurrentItem(0, true)
        }
        usedTabView = findViewById(R.id.used_tab_layout)
        usedTabView.setTitle(context.getString(R.string.used).uppercase())
        usedTabView.setOnClickListener {
            setSecondTabSelected()
            viewPager.setCurrentItem(1, true)
        }
        cancelledTabView = findViewById(R.id.cancelled_tab_layout)
        cancelledTabView.setTitle(context.getString(R.string.cancelled).uppercase())
        cancelledTabView.setOnClickListener {
            setThirdTabSelected()
            viewPager.setCurrentItem(2, true)
        }
        contentView = findViewById(R.id.content_layout)
        viewPager = contentView.findViewById(R.id.pager)
        viewPager.removeOverScroll()
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> setFirstTabSelected()
                    1 -> setSecondTabSelected()
                    else -> setThirdTabSelected()
                }
            }
        })
    }

    private fun setFirstTabSelected() {
        availableTabView.setTabSelected(true)
        usedTabView.setTabSelected(false)
        cancelledTabView.setTabSelected(false)
    }

    private fun setSecondTabSelected() {
        availableTabView.setTabSelected(false)
        usedTabView.setTabSelected(true)
        cancelledTabView.setTabSelected(false)
    }

    private fun setThirdTabSelected() {
        availableTabView.setTabSelected(false)
        usedTabView.setTabSelected(false)
        cancelledTabView.setTabSelected(true)
    }

    fun setUserData(helloText: String, userName: String) {
        helloTextView.text = helloText
        nameTextView.text = userName
    }

    fun setAvailableVouchersBadgeValue(voucherBadgeValue: String, vouchers: String) {
        voucherBadgeTextView.text = voucherBadgeValue
        vouchersTextView.text = vouchers
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        headerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                getStatusBarHeight() + convertDpToPx(100), MeasureSpec.EXACTLY
            )
        )

        helloTextView.measureWrapContent()

        val nameTextViewWidth = viewWidth - (helloTextView.measuredWidth + convertDpToPx(35))
        nameTextView.measure(
            MeasureSpec.makeMeasureSpec(nameTextViewWidth, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        youHaveTextView.measureWrapContent()
        voucherBadgeTextView.measureWrapContent()
        vouchersTextView.measureWrapContent()

        titleTextView.measureWrapContent()

        availableTabView.measureWrapContent()
        usedTabView.measureWrapContent()
        cancelledTabView.measureWrapContent()

        val contentViewWidth = viewWidth * 95 / 100
        val sideMargin = viewWidth * 5 / 100 / 2

        val tabWidthExtra = (contentViewWidth - (availableTabView.measuredWidth + usedTabView.measuredWidth +
                    cancelledTabView.measuredWidth + convertDpToPx(16))) / 3

        availableTabView.measure(
            MeasureSpec.makeMeasureSpec(
                availableTabView.measuredWidth + tabWidthExtra,
                MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        usedTabView.measure(
            MeasureSpec.makeMeasureSpec(
                usedTabView.measuredWidth + tabWidthExtra,
                MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        cancelledTabView.measure(
            MeasureSpec.makeMeasureSpec(
                cancelledTabView.measuredWidth + tabWidthExtra,
                MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        val contentViewHeight = viewHeight - (headerView.measuredHeight + availableTabView.measuredHeight +
                    sideMargin + convertDpToPx(50))
        contentView.measure(
            MeasureSpec.makeMeasureSpec(contentViewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(contentViewHeight, MeasureSpec.EXACTLY)
        )

        viewPager.measure(
            MeasureSpec.makeMeasureSpec(contentView.measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(contentView.measuredHeight, MeasureSpec.EXACTLY)
        )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        headerView.layoutToTopLeft(0, 0)

        helloTextView.layoutToTopLeft(
            convertDpToPx(15),
            getStatusBarHeight() + convertDpToPx(5)
        )

        nameTextView.layoutToTopLeft(
            helloTextView.right + convertDpToPx(5),
            helloTextView.top
        )

        youHaveTextView.layoutToTopLeft(
            helloTextView.left,
            helloTextView.bottom + convertDpToPx(5)
        )

        voucherBadgeTextView.layoutToTopLeft(
            youHaveTextView.right + convertDpToPx(3),
            youHaveTextView.top + (youHaveTextView.measuredHeight - voucherBadgeTextView.measuredHeight) / 2
        )

        vouchersTextView.layoutToTopLeft(
            voucherBadgeTextView.right + convertDpToPx(3),
            youHaveTextView.top
        )

        titleTextView.layoutToTopLeft(
            convertDpToPx(15),
            headerView.bottom + convertDpToPx(15)
        )

        val contentViewLeft = (viewWidth - contentView.measuredWidth) / 2
        availableTabView.layoutToTopLeft(
            contentViewLeft,
            titleTextView.bottom + convertDpToPx(13)
        )

        usedTabView.layoutToTopLeft(
            availableTabView.right + convertDpToPx(8),
            availableTabView.top
        )

        cancelledTabView.layoutToTopLeft(
            usedTabView.right + convertDpToPx(8),
            availableTabView.top
        )

        contentView.layoutToTopLeft(
            (viewWidth - contentView.measuredWidth) / 2,
            availableTabView.bottom - convertDpToPx(20)
        )

        viewPager.layoutToTopLeft(0, 0)
    }
}