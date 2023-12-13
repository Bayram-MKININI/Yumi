package net.noliaware.yumi.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.presentation.views.FillableTextWidget
import net.noliaware.yumi.commun.util.convertDpToPx
import net.noliaware.yumi.commun.util.getColorCompat
import net.noliaware.yumi.commun.util.getFontFromResources
import net.noliaware.yumi.commun.util.layoutToTopLeft
import net.noliaware.yumi.commun.util.layoutToTopRight
import net.noliaware.yumi.commun.util.measureWrapContent
import net.noliaware.yumi.commun.util.sizeForVisible
import kotlin.math.max

class ProfileView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var myDataTextView: TextView
    private lateinit var loginTitleTextView: TextView
    private lateinit var loginValueFillableTextWidget: FillableTextWidget
    private lateinit var surnameTitleTextView: TextView
    private lateinit var surnameValueFillableTextWidget: FillableTextWidget
    private lateinit var nameTitleTextView: TextView
    private lateinit var nameValueFillableTextWidget: FillableTextWidget
    private lateinit var referentTitleTextView: TextView
    private lateinit var referentValueFillableTextWidget: FillableTextWidget
    private lateinit var birthTitleTextView: TextView
    private lateinit var birthValueFillableTextWidget: FillableTextWidget
    private lateinit var phoneTitleTextView: TextView
    private lateinit var phoneValueFillableTextWidget: FillableTextWidget
    private lateinit var addressTitleTextView: TextView
    private lateinit var addressValueFillableTextWidget: FillableTextWidget

    private lateinit var separator1View: View
    private lateinit var boAccessTextView: TextView
    private lateinit var boAccessDescriptionFillableTextWidget: FillableTextWidget
    private lateinit var accessButtonLayout: LinearLayoutCompat

    private lateinit var separator2View: View
    private lateinit var myVouchersTextView: TextView
    private lateinit var emittedTitleTextView: TextView
    private lateinit var emittedValueFillableTextWidget: FillableTextWidget
    private lateinit var availableTitleTextView: TextView
    private lateinit var availableValueFillableTextWidget: FillableTextWidget
    private lateinit var usedTitleTextView: TextView
    private lateinit var usedValueFillableTextWidget: FillableTextWidget
    private lateinit var cancelledTitleTextView: TextView
    private lateinit var cancelledValueFillableTextWidget: FillableTextWidget
    private lateinit var privacyPolicyLinkTextView: TextView
    var callback: ProfileViewCallback? = null

    data class ProfileViewAdapter(
        val login: String = "",
        val surname: String = "",
        val name: String = "",
        val referent: String? = null,
        val birth: String = "",
        val phone: String = "",
        val address: String = "",
        val twoFactorAuthModeText: String = "",
        val twoFactorAuthModeActivated: Boolean = false,
        val emittedValue: String = "",
        val availableValue: String = "",
        val usedValue: String = "",
        val cancelledValue: String = ""
    )

    interface ProfileViewCallback {
        fun onGetCodeButtonClicked()
        fun onPrivacyPolicyButtonClicked()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        myDataTextView = findViewById(R.id.my_data_text_view)

        loginTitleTextView = findViewById(R.id.login_title_text_view)
        loginValueFillableTextWidget = findViewById(R.id.login_value_fillable_text_view)
        loginValueFillableTextWidget.setUpValueTextView()

        surnameTitleTextView = findViewById(R.id.surname_title_text_view)
        surnameValueFillableTextWidget = findViewById(R.id.surname_value_fillable_text_view)
        surnameValueFillableTextWidget.setUpValueTextView()

        nameTitleTextView = findViewById(R.id.name_title_text_view)
        nameValueFillableTextWidget = findViewById(R.id.name_value_fillable_text_view)
        nameValueFillableTextWidget.setUpValueTextView()

        referentTitleTextView = findViewById(R.id.referent_title_text_view)
        referentValueFillableTextWidget = findViewById(R.id.referent_value_fillable_text_view)
        referentValueFillableTextWidget.setUpValueTextView()

        birthTitleTextView = findViewById(R.id.birth_title_text_view)
        birthValueFillableTextWidget = findViewById(R.id.birth_value_fillable_text_view)
        birthValueFillableTextWidget.setUpValueTextView()

        phoneTitleTextView = findViewById(R.id.phone_title_text_view)
        phoneValueFillableTextWidget = findViewById(R.id.phone_value_fillable_text_view)
        phoneValueFillableTextWidget.setUpValueTextView()

        addressTitleTextView = findViewById(R.id.address_title_text_view)
        addressValueFillableTextWidget = findViewById(R.id.address_value_fillable_text_view)
        addressValueFillableTextWidget.setUpValueTextView()

        separator1View = findViewById(R.id.separator_1_view)
        boAccessTextView = findViewById(R.id.bo_access_text_view)

        boAccessDescriptionFillableTextWidget = findViewById(R.id.bo_access_description_fillable_text_view)
        boAccessDescriptionFillableTextWidget.setUpValueTextView()
        boAccessDescriptionFillableTextWidget.setFixedWidth(true)

        accessButtonLayout = findViewById(R.id.access_button_layout)
        accessButtonLayout.setOnClickListener {
            callback?.onGetCodeButtonClicked()
        }

        separator2View = findViewById(R.id.separator_2_view)
        myVouchersTextView = findViewById(R.id.my_vouchers_text_view)

        emittedTitleTextView = findViewById(R.id.emitted_title_text_view)
        emittedValueFillableTextWidget = findViewById(R.id.emitted_value_fillable_text_view)
        emittedValueFillableTextWidget.setUpValueTextView()

        availableTitleTextView = findViewById(R.id.available_title_text_view)
        availableValueFillableTextWidget = findViewById(R.id.available_value_fillable_text_view)
        availableValueFillableTextWidget.setUpValueTextView()

        usedTitleTextView = findViewById(R.id.used_title_text_view)
        usedValueFillableTextWidget = findViewById(R.id.used_value_fillable_text_view)
        usedValueFillableTextWidget.setUpValueTextView()

        cancelledTitleTextView = findViewById(R.id.cancelled_title_text_view)
        cancelledValueFillableTextWidget = findViewById(R.id.cancelled_value_fillable_text_view)
        cancelledValueFillableTextWidget.setUpValueTextView()

        privacyPolicyLinkTextView = findViewById(R.id.privacy_policy_link_text_view)
        privacyPolicyLinkTextView.setOnClickListener {
            callback?.onPrivacyPolicyButtonClicked()
        }
    }

    private fun FillableTextWidget.setUpValueTextView() {
        textView.apply {
            typeface = context.getFontFromResources(R.font.omnes_semibold_regular)
            setTextColor(context.getColorCompat(R.color.grey_2))
            textSize = 15f
        }
    }

    fun fillViewWithData(profileViewAdapter: ProfileViewAdapter) {

        loginValueFillableTextWidget.setText(profileViewAdapter.login)
        surnameValueFillableTextWidget.setText(profileViewAdapter.surname)
        nameValueFillableTextWidget.setText(profileViewAdapter.name)

        profileViewAdapter.referent?.let {
            referentTitleTextView.isVisible = true
            referentValueFillableTextWidget.isVisible = true
            referentValueFillableTextWidget.setText(profileViewAdapter.referent)
        }

        birthValueFillableTextWidget.setText(profileViewAdapter.birth)
        phoneValueFillableTextWidget.setText(profileViewAdapter.phone)
        addressValueFillableTextWidget.setText(profileViewAdapter.address)

        boAccessDescriptionFillableTextWidget.setText(profileViewAdapter.twoFactorAuthModeText)
        if (profileViewAdapter.twoFactorAuthModeActivated) {
            boAccessDescriptionFillableTextWidget.textView.gravity = Gravity.CENTER
        }
        accessButtonLayout.isVisible = profileViewAdapter.twoFactorAuthModeActivated

        emittedValueFillableTextWidget.setText(profileViewAdapter.emittedValue)
        availableValueFillableTextWidget.setText(profileViewAdapter.availableValue)
        usedValueFillableTextWidget.setText(profileViewAdapter.usedValue)
        cancelledValueFillableTextWidget.setText(profileViewAdapter.cancelledValue)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        var viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        myDataTextView.measureWrapContent()

        loginTitleTextView.measureWrapContent()
        loginValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 5 / 10)

        surnameTitleTextView.measureWrapContent()
        surnameValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 2 / 10)

        nameTitleTextView.measureWrapContent()
        nameValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 2 / 10)

        if (referentTitleTextView.isVisible) {
            referentTitleTextView.measureWrapContent()
            referentValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 3 / 10)
        }

        birthTitleTextView.measureWrapContent()
        birthValueFillableTextWidget.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 1 / 2, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(35), MeasureSpec.EXACTLY)
        )

        phoneTitleTextView.measureWrapContent()
        phoneValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 3 / 10)

        addressTitleTextView.measureWrapContent()
        addressValueFillableTextWidget.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 1 / 2, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(35), MeasureSpec.EXACTLY)
        )

        separator1View.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 4 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(3), MeasureSpec.EXACTLY)
        )

        boAccessTextView.measureWrapContent()
        boAccessDescriptionFillableTextWidget.measure(
            MeasureSpec.makeMeasureSpec(viewWidth - convertDpToPx(40), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(35), MeasureSpec.EXACTLY)
        )
        if (accessButtonLayout.isVisible) {
            accessButtonLayout.measureWrapContent()
        }

        separator2View.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 4 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(3), MeasureSpec.EXACTLY)
        )

        myVouchersTextView.measureWrapContent()

        emittedTitleTextView.measureWrapContent()
        emittedValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 3 / 10)

        availableTitleTextView.measureWrapContent()
        availableValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 3 / 10)

        usedTitleTextView.measureWrapContent()
        usedValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 3 / 10)

        cancelledTitleTextView.measureWrapContent()
        cancelledValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 3 / 10)

        privacyPolicyLinkTextView.measureWrapContent()

        viewHeight = myDataTextView.measuredHeight +
                max(loginTitleTextView.measuredHeight, loginValueFillableTextWidget.measuredHeight) +
                max(surnameTitleTextView.measuredHeight, surnameValueFillableTextWidget.measuredHeight) +
                max(nameTitleTextView.measuredHeight, nameValueFillableTextWidget.measuredHeight) +
                referentTitleTextView.sizeForVisible {
                    referentValueFillableTextWidget.measuredHeight + convertDpToPx(15)
                } +
                max(birthTitleTextView.measuredHeight, birthValueFillableTextWidget.measuredHeight) +
                max(phoneTitleTextView.measuredHeight, phoneValueFillableTextWidget.measuredHeight) +
                max(addressTitleTextView.measuredHeight, addressValueFillableTextWidget.measuredHeight) +
                separator1View.measuredHeight + boAccessTextView.measuredHeight +
                boAccessDescriptionFillableTextWidget.measuredHeight +
                accessButtonLayout.sizeForVisible {
                    accessButtonLayout.measuredHeight + convertDpToPx(15)
                } + separator2View.measuredHeight + myVouchersTextView.measuredHeight +
                max(emittedTitleTextView.measuredHeight, emittedValueFillableTextWidget.measuredHeight) +
                max(availableTitleTextView.measuredHeight, availableValueFillableTextWidget.measuredHeight) +
                max(usedTitleTextView.measuredHeight, usedValueFillableTextWidget.measuredHeight) +
                max(cancelledTitleTextView.measuredHeight, cancelledValueFillableTextWidget.measuredHeight) +
                privacyPolicyLinkTextView.measuredHeight + convertDpToPx(215)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    private fun View.measureTextWidgetWithWidth(width: Int) {
        measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(18), MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        myDataTextView.layoutToTopLeft(
            convertDpToPx(20),
            0
        )

        val edge = viewWidth * 1 / 3

        loginTitleTextView.layoutToTopRight(
            edge,
            myDataTextView.bottom + convertDpToPx(15)
        )

        loginValueFillableTextWidget.layoutToTopLeft(
            loginTitleTextView.right + convertDpToPx(15),
            loginTitleTextView.top + (loginTitleTextView.measuredHeight - loginValueFillableTextWidget.measuredHeight) / 2
        )

        surnameTitleTextView.layoutToTopRight(
            edge,
            loginTitleTextView.bottom + convertDpToPx(10)
        )

        surnameValueFillableTextWidget.layoutToTopLeft(
            surnameTitleTextView.right + convertDpToPx(15),
            surnameTitleTextView.top + (surnameTitleTextView.measuredHeight - surnameValueFillableTextWidget.measuredHeight) / 2
        )

        nameTitleTextView.layoutToTopRight(
            edge,
            surnameTitleTextView.bottom + convertDpToPx(10)
        )

        nameValueFillableTextWidget.layoutToTopLeft(
            nameTitleTextView.right + convertDpToPx(15),
            nameTitleTextView.top + (nameTitleTextView.measuredHeight - nameValueFillableTextWidget.measuredHeight) / 2
        )

        val referentViewBottom = if (referentTitleTextView.isVisible) {

            referentTitleTextView.layoutToTopRight(
                edge,
                nameValueFillableTextWidget.bottom + convertDpToPx(10)
            )

            referentValueFillableTextWidget.layoutToTopLeft(
                referentTitleTextView.right + convertDpToPx(15),
                referentTitleTextView.top + (referentTitleTextView.measuredHeight - referentValueFillableTextWidget.measuredHeight) / 2
            )

            referentTitleTextView.bottom

        } else {
            nameTitleTextView.bottom
        }

        birthTitleTextView.layoutToTopRight(
            edge,
            referentViewBottom + convertDpToPx(10)
        )

        birthValueFillableTextWidget.layoutToTopLeft(
            birthTitleTextView.right + convertDpToPx(15),
            birthTitleTextView.top + (birthTitleTextView.measuredHeight - birthValueFillableTextWidget.measuredHeight) / 2
        )

        phoneTitleTextView.layoutToTopRight(
            edge,
            max(birthTitleTextView.bottom, birthValueFillableTextWidget.bottom) + convertDpToPx(10)
        )

        phoneValueFillableTextWidget.layoutToTopLeft(
            phoneTitleTextView.right + convertDpToPx(15),
            phoneTitleTextView.top + (phoneTitleTextView.measuredHeight - phoneValueFillableTextWidget.measuredHeight) / 2
        )

        addressTitleTextView.layoutToTopRight(
            edge,
            phoneTitleTextView.bottom + convertDpToPx(10)
        )

        addressValueFillableTextWidget.layoutToTopLeft(
            addressTitleTextView.right + convertDpToPx(15),
            addressTitleTextView.top
        )

        separator1View.layoutToTopLeft(
            (viewWidth - separator1View.measuredWidth) / 2,
            max(addressTitleTextView.bottom, addressValueFillableTextWidget.bottom) + convertDpToPx(15)
        )

        boAccessTextView.layoutToTopLeft(
            myDataTextView.left,
            separator1View.bottom + convertDpToPx(15)
        )

        boAccessDescriptionFillableTextWidget.layoutToTopLeft(
            myDataTextView.left,
            boAccessTextView.bottom + convertDpToPx(10)
        )

        val boAccessBottom = if (accessButtonLayout.isVisible) {
            accessButtonLayout.layoutToTopLeft(
                (viewWidth - accessButtonLayout.measuredWidth) / 2,
                boAccessDescriptionFillableTextWidget.bottom + convertDpToPx(15)
            )
            accessButtonLayout.bottom
        } else {
            boAccessDescriptionFillableTextWidget.bottom
        }

        separator2View.layoutToTopLeft(
            (viewWidth - separator2View.measuredWidth) / 2,
            boAccessBottom + convertDpToPx(20)
        )

        myVouchersTextView.layoutToTopLeft(
            myDataTextView.left,
            separator2View.bottom + convertDpToPx(15)
        )

        emittedTitleTextView.layoutToTopRight(
            edge,
            myVouchersTextView.bottom + convertDpToPx(10)
        )

        emittedValueFillableTextWidget.layoutToTopLeft(
            emittedTitleTextView.right + convertDpToPx(15),
            emittedTitleTextView.top + (emittedTitleTextView.measuredHeight - emittedValueFillableTextWidget.measuredHeight) / 2
        )

        availableTitleTextView.layoutToTopRight(
            edge,
            emittedTitleTextView.bottom + convertDpToPx(10)
        )

        availableValueFillableTextWidget.layoutToTopLeft(
            availableTitleTextView.right + convertDpToPx(15),
            availableTitleTextView.top + (availableTitleTextView.measuredHeight - availableValueFillableTextWidget.measuredHeight) / 2
        )

        usedTitleTextView.layoutToTopRight(
            edge,
            availableTitleTextView.bottom + convertDpToPx(10)
        )

        usedValueFillableTextWidget.layoutToTopLeft(
            usedTitleTextView.right + convertDpToPx(15),
            usedTitleTextView.top + (usedTitleTextView.measuredHeight - usedValueFillableTextWidget.measuredHeight) / 2
        )

        cancelledTitleTextView.layoutToTopRight(
            edge,
            usedTitleTextView.bottom + convertDpToPx(10)
        )

        cancelledValueFillableTextWidget.layoutToTopLeft(
            cancelledTitleTextView.right + convertDpToPx(15),
            cancelledTitleTextView.top + (cancelledTitleTextView.measuredHeight - cancelledValueFillableTextWidget.measuredHeight) / 2
        )

        privacyPolicyLinkTextView.layoutToTopLeft(
            (viewWidth - privacyPolicyLinkTextView.measuredWidth) / 2,
            cancelledTitleTextView.bottom + convertDpToPx(30)
        )
    }
}