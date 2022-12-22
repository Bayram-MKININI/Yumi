package net.noliaware.yumi.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.presentation.adapters.BaseAdapter
import net.noliaware.yumi.commun.presentation.views.DataValueView
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView
import net.noliaware.yumi.feature_categories.presentation.views.CategoryItemView.CategoryItemViewAdapter

class ProfileView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var myDataTextView: TextView
    private lateinit var myDataLinearLayout: LinearLayoutCompat
    private lateinit var complementaryDataTextView: TextView
    private lateinit var complementaryDataLinearLayout: LinearLayoutCompat
    private lateinit var getCodeTextView: TextView
    private lateinit var myHistoryTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private val categoryItemViewAdapters = mutableListOf<CategoryItemViewAdapter>()
    var callback: ProfileViewCallback? by weak()

    data class ProfileViewAdapter(
        val myDataAdapters: MutableList<DataValueView.DataValueViewAdapter> = mutableListOf(),
        val complementaryDataAdapters: MutableList<DataValueView.DataValueViewAdapter> = mutableListOf(),
        val categoryItemViewAdapters: MutableList<CategoryItemViewAdapter> = mutableListOf()
    )

    interface ProfileViewCallback {
        fun onGetCodeButtonClicked()
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
        getCodeTextView = findViewById(R.id.get_code_text_view)
        getCodeTextView.setOnClickListener { callback?.onGetCodeButtonClicked() }
        myHistoryTextView = findViewById(R.id.my_history_text_view)

        recyclerView = findViewById(R.id.recycler_view)

        val adapter = BaseAdapter(categoryItemViewAdapters)

        adapter.expressionViewHolderBinding = { eachItem, view ->
            (view as CategoryItemView).fillViewWithData(eachItem)
        }

        adapter.expressionOnCreateViewHolder = { viewGroup ->
            viewGroup.inflate(R.layout.category_item_layout, false)
        }

        recyclerView.also {

            it.layoutManager = GridLayoutManager(
                context,
                context.resources.getInteger(R.integer.number_of_columns_for_categories)
            )

            val spacing = convertDpToPx(10)

            it.setPadding(spacing, spacing, spacing, spacing)
            it.clipToPadding = false
            it.clipChildren = false
            it.addItemDecoration(MarginItemDecoration(spacing, GRID))

            it.adapter = adapter

            it.onItemClicked(onClick = { position, _ ->
                callback?.onCategoryClickedAtIndex(position)
            })
        }
    }

    fun fillViewWithData(profileViewAdapter: ProfileViewAdapter) {

        if (myDataLinearLayout.isNotEmpty()) {
            myDataLinearLayout.removeAllViews()
        }

        profileViewAdapter.myDataAdapters.forEach { profileDataViewAdapter ->
            DataValueView(context).also {
                it.fillViewWithData(profileDataViewAdapter)
                myDataLinearLayout.addView(it)
            }
        }

        if (complementaryDataLinearLayout.isNotEmpty()) {
            complementaryDataLinearLayout.removeAllViews()
        }

        profileViewAdapter.complementaryDataAdapters.forEach { profileDataViewAdapter ->
            DataValueView(context).also {
                it.fillViewWithData(profileDataViewAdapter)
                complementaryDataLinearLayout.addView(it)
            }
        }

        myHistoryTextView.isVisible = profileViewAdapter.categoryItemViewAdapters.isNotEmpty()

        if (profileViewAdapter.categoryItemViewAdapters.isNotEmpty()) {

            if (categoryItemViewAdapters.isNotEmpty())
                categoryItemViewAdapters.clear()

            categoryItemViewAdapters.addAll(profileViewAdapter.categoryItemViewAdapters)
            recyclerView.adapter?.notifyDataSetChanged()
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

        getCodeTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 7 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(40), MeasureSpec.EXACTLY)
        )

        myHistoryTextView.measureWrapContent()

        recyclerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        viewHeight = myDataTextView.measuredHeight + myDataLinearLayout.measuredHeight + complementaryDataTextView.measuredHeight +
                    complementaryDataLinearLayout.measuredHeight + getCodeTextView.measuredHeight + myHistoryTextView.measuredHeight +
                    recyclerView.measuredHeight + convertDpToPx(100)

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

        getCodeTextView.layoutToTopLeft(
            (viewWidth - getCodeTextView.measuredWidth) / 2,
            complementaryDataLinearLayout.bottom + convertDpToPx(10)
        )

        myHistoryTextView.layoutToTopLeft(
            convertDpToPx(20),
            getCodeTextView.bottom + convertDpToPx(10)
        )

        recyclerView.layoutToTopLeft(
            0,
            myHistoryTextView.bottom + convertDpToPx(10)
        )
    }
}