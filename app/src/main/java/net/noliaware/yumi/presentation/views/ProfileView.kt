package net.noliaware.yumi.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.presentation.views.CategoryItemView.CategoryItemViewAdapter
import net.noliaware.yumi.presentation.views.ProfileDataView.ProfileDataViewAdapter

class ProfileView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var myDataTextView: TextView
    private lateinit var myDataLinearLayout: LinearLayoutCompat
    private lateinit var complementaryDataTextView: TextView
    private lateinit var complementaryDataLinearLayout: LinearLayoutCompat
    private lateinit var myHistoryTextView: TextView
    private lateinit var categoriesDashboardLayout: DashboardLayout
    var callback: ProfileViewCallback? by weak()

    data class ProfileViewAdapter(
        val myDataAdapters: MutableList<ProfileDataViewAdapter> = mutableListOf(),
        val complementaryDataAdapters: MutableList<ProfileDataViewAdapter> = mutableListOf(),
        val categoryItemViewAdapters: MutableList<CategoryItemViewAdapter> = mutableListOf()
    )

    interface ProfileViewCallback {
        fun onCategoryClickedAtIndex(index: Int)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        myDataTextView = findViewById(R.id.my_data_text_view)
        myDataLinearLayout = findViewById(R.id.my_data_linear_layout)
        complementaryDataTextView = findViewById(R.id.complementary_data_text_view)
        complementaryDataLinearLayout = findViewById(R.id.complementary_data_linear_layout)
        myHistoryTextView = findViewById(R.id.my_history_text_view)
        categoriesDashboardLayout = findViewById(R.id.categories_view)
    }

    fun fillViewWithData(profileViewAdapter: ProfileViewAdapter) {

        profileViewAdapter.myDataAdapters.forEach { profileDataViewAdapter ->
            ProfileDataView(context).also {
                it.fillViewWithData(profileDataViewAdapter)
                myDataLinearLayout.addView(it)
            }
        }

        profileViewAdapter.complementaryDataAdapters.forEach { profileDataViewAdapter ->
            ProfileDataView(context).also {
                it.fillViewWithData(profileDataViewAdapter)
                complementaryDataLinearLayout.addView(it)
            }
        }

        profileViewAdapter.categoryItemViewAdapters.forEachIndexed { index, categoryItemViewAdapter ->

            inflate(R.layout.category_item_layout, false).also {
                val categoryItemView = it as CategoryItemView
                categoryItemView.setTag(R.string.view_tag_key, index)
                categoryItemView.fillViewWithData(categoryItemViewAdapter)
                categoryItemView.setOnClickListener(onClickListener)
                categoriesDashboardLayout.addView(it)
            }
        }
    }

    private val onClickListener: OnClickListener by lazy {
        OnClickListener { view ->
            callback?.onCategoryClickedAtIndex(view.getTag(R.string.view_tag_key).toString().toInt())
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        var viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        myDataTextView.measureWrapContent()

        myDataLinearLayout.measure(
            MeasureSpec.makeMeasureSpec(viewWidth - convertDpToPx(40), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        complementaryDataTextView.measureWrapContent()

        complementaryDataLinearLayout.measure(
            MeasureSpec.makeMeasureSpec(viewWidth - convertDpToPx(40), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        myHistoryTextView.measureWrapContent()

        categoriesDashboardLayout.measure(
            MeasureSpec.makeMeasureSpec(viewWidth - convertDpToPx(40), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        viewHeight =
            myDataTextView.measuredHeight + myDataLinearLayout.measuredHeight + complementaryDataTextView.measuredHeight + complementaryDataLinearLayout.measuredHeight + myHistoryTextView.measuredHeight + categoriesDashboardLayout.measuredHeight + convertDpToPx(
                80
            )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        myDataTextView.layoutToTopLeft(
            convertDpToPx(20),
            convertDpToPx(10)
        )

        myDataLinearLayout.layoutToTopLeft(
            convertDpToPx(20),
            myDataTextView.bottom + convertDpToPx(10)
        )

        complementaryDataTextView.layoutToTopLeft(
            convertDpToPx(20),
            myDataLinearLayout.bottom + convertDpToPx(10)
        )

        complementaryDataLinearLayout.layoutToTopLeft(
            convertDpToPx(20),
            complementaryDataTextView.bottom + convertDpToPx(10)
        )

        myHistoryTextView.layoutToTopLeft(
            convertDpToPx(20),
            complementaryDataLinearLayout.bottom + convertDpToPx(10)
        )

        categoriesDashboardLayout.layoutToTopLeft(
            convertDpToPx(20),
            myHistoryTextView.bottom + convertDpToPx(20)
        )
    }
}