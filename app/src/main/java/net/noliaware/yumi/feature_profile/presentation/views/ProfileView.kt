package net.noliaware.yumi.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.presentation.views.ClipartTabView
import net.noliaware.yumi.commun.util.convertDpToPx
import net.noliaware.yumi.commun.util.getStatusBarHeight
import net.noliaware.yumi.commun.util.layoutToTopLeft
import net.noliaware.yumi.commun.util.measureWrapContent

class ProfileView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var headerView: View
    private lateinit var titleTextView: TextView
    private lateinit var profileIconView: View
    private lateinit var myDataTabView: ClipartTabView
    private lateinit var myVouchersTabView: ClipartTabView
    private lateinit var contentView: View
    private lateinit var viewPager: ViewPager2

    val getViewPager get() = viewPager

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        headerView = findViewById(R.id.header_view)
        titleTextView = findViewById(R.id.title_text_view)
        profileIconView = findViewById(R.id.profile_icon_view)
        myDataTabView = findViewById(R.id.my_data_tab_layout)
        myDataTabView.setTitle(context.getString(R.string.my_data).uppercase())
        myDataTabView.setOnClickListener {
            setFirstTabSelected()
            viewPager.setCurrentItem(0, true)
        }
        myVouchersTabView = findViewById(R.id.my_vouchers_tab_layout)
        myVouchersTabView.setTitle(context.getString(R.string.my_vouchers).uppercase())
        myVouchersTabView.setOnClickListener {
            setSecondTabSelected()
            viewPager.setCurrentItem(1, true)
        }
        contentView = findViewById(R.id.content_layout)
        viewPager = contentView.findViewById(R.id.pager)
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    setFirstTabSelected()
                } else {
                    setSecondTabSelected()
                }
            }
        })
    }

    private fun setFirstTabSelected() {
        myVouchersTabView.setTabSelected(false)
        myDataTabView.setTabSelected(true)
    }

    private fun setSecondTabSelected() {
        myDataTabView.setTabSelected(false)
        myVouchersTabView.setTabSelected(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        headerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                getStatusBarHeight() + convertDpToPx(75),
                MeasureSpec.EXACTLY
            )
        )

        titleTextView.measureWrapContent()
        profileIconView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY)
        )

        val contentViewWidth = viewWidth * 9 / 10

        myDataTabView.measureWrapContent()
        myVouchersTabView.measureWrapContent()

        val tabWidthExtra = (contentViewWidth - (myDataTabView.measuredWidth + myVouchersTabView.measuredWidth + convertDpToPx(
                8
            ))) / 2

        myDataTabView.measure(
            MeasureSpec.makeMeasureSpec(
                myDataTabView.measuredWidth + tabWidthExtra,
                MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        myVouchersTabView.measure(
            MeasureSpec.makeMeasureSpec(
                myVouchersTabView.measuredWidth + tabWidthExtra,
                MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        val contentViewHeight = viewHeight - (headerView.measuredHeight + profileIconView.measuredHeight / 2 +
                    myDataTabView.measuredHeight + convertDpToPx(35))

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

        titleTextView.layoutToTopLeft(
            (viewWidth - titleTextView.measuredWidth) / 2,
            getStatusBarHeight() + convertDpToPx(15)
        )

        profileIconView.layoutToTopLeft(
            (viewWidth - profileIconView.measuredWidth) / 2,
            headerView.bottom - profileIconView.measuredHeight / 2
        )

        val contentViewLeft = (viewWidth - contentView.measuredWidth) / 2
        myDataTabView.layoutToTopLeft(
            contentViewLeft,
            profileIconView.bottom + convertDpToPx(15)
        )

        myVouchersTabView.layoutToTopLeft(
            myDataTabView.right + convertDpToPx(8),
            myDataTabView.top
        )

        contentView.layoutToTopLeft(
            (viewWidth - contentView.measuredWidth) / 2,
            myDataTabView.bottom - convertDpToPx(20)
        )

        viewPager.layoutToTopLeft(0, 0)
    }
}