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
import net.noliaware.yumi.commun.util.convertDpToPx
import net.noliaware.yumi.commun.util.layoutToTopLeft
import net.noliaware.yumi.commun.util.layoutToTopRight
import net.noliaware.yumi.commun.util.measureWrapContent
import net.noliaware.yumi.commun.util.weak
import kotlin.math.max

class ProfileView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var myDataTextView: TextView
    private lateinit var loginTitleTextView: TextView
    private lateinit var loginValueTextView: TextView
    private lateinit var surnameTitleTextView: TextView
    private lateinit var surnameValueTextView: TextView
    private lateinit var nameTitleTextView: TextView
    private lateinit var nameValueTextView: TextView
    private lateinit var referentTitleTextView: TextView
    private lateinit var referentValueTextView: TextView
    private lateinit var birthTitleTextView: TextView
    private lateinit var birthValueTextView: TextView
    private lateinit var phoneTitleTextView: TextView
    private lateinit var phoneValueTextView: TextView
    private lateinit var addressTitleTextView: TextView
    private lateinit var addressValueTextView: TextView

    private lateinit var separator1View: View
    private lateinit var boAccessTextView: TextView
    private lateinit var boAccessDescriptionTextView: TextView
    private lateinit var accessButtonLayout: LinearLayoutCompat

    private lateinit var separator2View: View
    private lateinit var myVouchersTextView: TextView
    private lateinit var emittedTitleTextView: TextView
    private lateinit var emittedValueTextView: TextView
    private lateinit var availableTitleTextView: TextView
    private lateinit var availableValueTextView: TextView
    private lateinit var usedTitleTextView: TextView
    private lateinit var usedValueTextView: TextView
    private lateinit var cancelledTitleTextView: TextView
    private lateinit var cancelledValueTextView: TextView
    private lateinit var privacyPolicyLinkTextView: TextView
    var callback: ProfileParentViewCallback? by weak()

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

    interface ProfileParentViewCallback {
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
        loginValueTextView = findViewById(R.id.login_value_text_view)
        surnameTitleTextView = findViewById(R.id.surname_title_text_view)
        surnameValueTextView = findViewById(R.id.surname_value_text_view)
        nameTitleTextView = findViewById(R.id.name_title_text_view)
        nameValueTextView = findViewById(R.id.name_value_text_view)
        referentTitleTextView = findViewById(R.id.referent_title_text_view)
        referentValueTextView = findViewById(R.id.referent_value_text_view)
        birthTitleTextView = findViewById(R.id.birth_title_text_view)
        birthValueTextView = findViewById(R.id.birth_value_text_view)
        phoneTitleTextView = findViewById(R.id.phone_title_text_view)
        phoneValueTextView = findViewById(R.id.phone_value_text_view)
        addressTitleTextView = findViewById(R.id.address_title_text_view)
        addressValueTextView = findViewById(R.id.address_value_text_view)

        separator1View = findViewById(R.id.separator_1_view)
        boAccessTextView = findViewById(R.id.bo_access_text_view)
        boAccessDescriptionTextView = findViewById(R.id.bo_access_description_text_view)
        accessButtonLayout = findViewById(R.id.access_button_layout)
        accessButtonLayout.setOnClickListener {
            callback?.onGetCodeButtonClicked()
        }

        separator2View = findViewById(R.id.separator_2_view)
        myVouchersTextView = findViewById(R.id.my_vouchers_text_view)
        emittedTitleTextView = findViewById(R.id.emitted_title_text_view)
        emittedValueTextView = findViewById(R.id.emitted_value_text_view)
        availableTitleTextView = findViewById(R.id.available_title_text_view)
        availableValueTextView = findViewById(R.id.available_value_text_view)
        usedTitleTextView = findViewById(R.id.used_title_text_view)
        usedValueTextView = findViewById(R.id.used_value_text_view)
        cancelledTitleTextView = findViewById(R.id.cancelled_title_text_view)
        cancelledValueTextView = findViewById(R.id.cancelled_value_text_view)
        privacyPolicyLinkTextView = findViewById(R.id.privacy_policy_link_text_view)
        privacyPolicyLinkTextView.setOnClickListener {
            callback?.onPrivacyPolicyButtonClicked()
        }
    }

    fun fillViewWithData(profileViewAdapter: ProfileViewAdapter) {

        loginValueTextView.text = profileViewAdapter.login
        surnameValueTextView.text = profileViewAdapter.surname
        nameValueTextView.text = profileViewAdapter.name

        profileViewAdapter.referent?.let {
            referentTitleTextView.isVisible = true
            referentValueTextView.isVisible = true
            referentValueTextView.text = profileViewAdapter.referent
        }

        birthValueTextView.text = profileViewAdapter.birth
        phoneValueTextView.text = profileViewAdapter.phone
        addressValueTextView.text = profileViewAdapter.address

        boAccessDescriptionTextView.text = profileViewAdapter.twoFactorAuthModeText
        if (profileViewAdapter.twoFactorAuthModeActivated) {
            boAccessDescriptionTextView.gravity = Gravity.CENTER
        }
        accessButtonLayout.isVisible = profileViewAdapter.twoFactorAuthModeActivated

        emittedValueTextView.text = profileViewAdapter.emittedValue
        availableValueTextView.text = profileViewAdapter.availableValue
        usedValueTextView.text = profileViewAdapter.usedValue
        cancelledValueTextView.text = profileViewAdapter.cancelledValue
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        var viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        myDataTextView.measureWrapContent()

        loginTitleTextView.measureWrapContent()
        loginValueTextView.measureWrapContent()

        surnameTitleTextView.measureWrapContent()
        surnameValueTextView.measureWrapContent()

        nameTitleTextView.measureWrapContent()
        nameValueTextView.measureWrapContent()

        if (referentTitleTextView.isVisible) {
            referentTitleTextView.measureWrapContent()
            referentValueTextView.measureWrapContent()
        }

        birthTitleTextView.measureWrapContent()
        birthValueTextView.measureWrapContent()

        phoneTitleTextView.measureWrapContent()
        phoneValueTextView.measureWrapContent()

        addressTitleTextView.measureWrapContent()
        addressValueTextView.measureWrapContent()

        separator1View.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 4 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(3), MeasureSpec.EXACTLY)
        )

        boAccessTextView.measureWrapContent()
        boAccessDescriptionTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth - convertDpToPx(40), MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
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
        emittedValueTextView.measureWrapContent()

        availableTitleTextView.measureWrapContent()
        availableValueTextView.measureWrapContent()

        usedTitleTextView.measureWrapContent()
        usedValueTextView.measureWrapContent()

        cancelledTitleTextView.measureWrapContent()
        cancelledValueTextView.measureWrapContent()

        privacyPolicyLinkTextView.measureWrapContent()

        viewHeight = myDataTextView.measuredHeight + loginValueTextView.measuredHeight +
                surnameValueTextView.measuredHeight + nameValueTextView.measuredHeight +
                if (referentTitleTextView.isVisible) {
                    referentValueTextView.measuredHeight + convertDpToPx(15)
                } else {
                    0
                } +
                birthValueTextView.measuredHeight + phoneValueTextView.measuredHeight +
                addressValueTextView.measuredHeight + separator1View.measuredHeight +
                boAccessTextView.measuredHeight + boAccessDescriptionTextView.measuredHeight +
                if (accessButtonLayout.isVisible) {
                    accessButtonLayout.measuredHeight + convertDpToPx(15)
                } else {
                    0
                } +
                separator2View.measuredHeight + myVouchersTextView.measuredHeight +
                emittedValueTextView.measuredHeight + availableValueTextView.measuredHeight +
                usedValueTextView.measuredHeight + cancelledValueTextView.measuredHeight +
                privacyPolicyLinkTextView.measuredHeight + convertDpToPx(215)

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
            0
        )

        val edge = viewWidth * 1 / 3

        loginTitleTextView.layoutToTopRight(
            edge,
            myDataTextView.bottom + convertDpToPx(15)
        )

        loginValueTextView.layoutToTopLeft(
            loginTitleTextView.right + convertDpToPx(15),
            loginTitleTextView.top
        )

        surnameTitleTextView.layoutToTopRight(
            edge,
            loginTitleTextView.bottom + convertDpToPx(10)
        )

        surnameValueTextView.layoutToTopLeft(
            surnameTitleTextView.right + convertDpToPx(15),
            surnameTitleTextView.top
        )

        nameTitleTextView.layoutToTopRight(
            edge,
            surnameTitleTextView.bottom + convertDpToPx(10)
        )

        nameValueTextView.layoutToTopLeft(
            nameTitleTextView.right + convertDpToPx(15),
            nameTitleTextView.top
        )

        val referentViewBottom = if (referentTitleTextView.isVisible) {

            referentTitleTextView.layoutToTopRight(
                edge,
                nameValueTextView.bottom + convertDpToPx(10)
            )

            referentValueTextView.layoutToTopLeft(
                referentTitleTextView.right + convertDpToPx(15),
                referentTitleTextView.top
            )

            referentTitleTextView.bottom

        } else {
            nameTitleTextView.bottom
        }

        birthTitleTextView.layoutToTopRight(
            edge,
            referentViewBottom + convertDpToPx(10)
        )

        birthValueTextView.layoutToTopLeft(
            birthTitleTextView.right + convertDpToPx(15),
            birthTitleTextView.top
        )

        phoneTitleTextView.layoutToTopRight(
            edge,
            max(birthTitleTextView.bottom, birthValueTextView.bottom) + convertDpToPx(10)
        )

        phoneValueTextView.layoutToTopLeft(
            phoneTitleTextView.right + convertDpToPx(15),
            phoneTitleTextView.top
        )

        addressTitleTextView.layoutToTopRight(
            edge,
            phoneTitleTextView.bottom + convertDpToPx(10)
        )

        addressValueTextView.layoutToTopLeft(
            addressTitleTextView.right + convertDpToPx(15),
            addressTitleTextView.top
        )

        separator1View.layoutToTopLeft(
            (viewWidth - separator1View.measuredWidth) / 2,
            max(addressTitleTextView.bottom, addressValueTextView.bottom) + convertDpToPx(15)
        )

        boAccessTextView.layoutToTopLeft(
            myDataTextView.left,
            separator1View.bottom + convertDpToPx(15)
        )

        boAccessDescriptionTextView.layoutToTopLeft(
            myDataTextView.left,
            boAccessTextView.bottom + convertDpToPx(10)
        )

        val boAccessBottom = if (accessButtonLayout.isVisible) {
            accessButtonLayout.layoutToTopLeft(
                (viewWidth - accessButtonLayout.measuredWidth) / 2,
                boAccessDescriptionTextView.bottom + convertDpToPx(15)
            )
            accessButtonLayout.bottom
        } else {
            boAccessDescriptionTextView.bottom
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

        emittedValueTextView.layoutToTopLeft(
            emittedTitleTextView.right + convertDpToPx(15),
            emittedTitleTextView.top
        )

        availableTitleTextView.layoutToTopRight(
            edge,
            emittedTitleTextView.bottom + convertDpToPx(10)
        )

        availableValueTextView.layoutToTopLeft(
            availableTitleTextView.right + convertDpToPx(15),
            availableTitleTextView.top
        )

        usedTitleTextView.layoutToTopRight(
            edge,
            availableTitleTextView.bottom + convertDpToPx(10)
        )

        usedValueTextView.layoutToTopLeft(
            usedTitleTextView.right + convertDpToPx(15),
            usedTitleTextView.top
        )

        cancelledTitleTextView.layoutToTopRight(
            edge,
            usedTitleTextView.bottom + convertDpToPx(10)
        )

        cancelledValueTextView.layoutToTopLeft(
            cancelledTitleTextView.right + convertDpToPx(15),
            cancelledTitleTextView.top
        )

        privacyPolicyLinkTextView.layoutToTopLeft(
            (viewWidth - privacyPolicyLinkTextView.measuredWidth) / 2,
            cancelledTitleTextView.bottom + convertDpToPx(30)
        )
    }
}