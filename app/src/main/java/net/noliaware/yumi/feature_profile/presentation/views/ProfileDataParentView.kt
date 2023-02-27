package net.noliaware.yumi.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.*

class ProfileDataParentView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var contentView: View
    private lateinit var profileDataView: ProfileDataView
    private lateinit var getCodeLayout: LinearLayoutCompat

    var callback: ProfileViewCallback? by weak()

    data class ProfileViewAdapter(
        val login: String = "",
        val surname: String = "",
        val name: String = "",
        val referent: String? = null,
        val birth: String = "",
        val phone: String = "",
        val address: String = ""
    )

    interface ProfileViewCallback {
        fun onGetCodeButtonClicked()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        contentView = findViewById(R.id.content_view)
        profileDataView = findViewById(R.id.profile_data_view)
        getCodeLayout = findViewById(R.id.get_code_layout)
        getCodeLayout.setOnClickListener { callback?.onGetCodeButtonClicked() }
    }

    fun fillViewWithData(profileViewAdapter: ProfileViewAdapter) {
        profileDataView.fillViewWithData(profileViewAdapter)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        getCodeLayout.measureWrapContent()

        val contentViewHeight = viewHeight - (getCodeLayout.measuredHeight + convertDpToPx(40))
        contentView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(contentViewHeight, MeasureSpec.EXACTLY)
        )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        contentView.layoutToTopLeft(0, 0)

        getCodeLayout.layoutToBottomLeft(
            (viewWidth - getCodeLayout.measuredWidth) / 2,
            bottom - convertDpToPx(40)
        )
    }
}