package net.noliaware.yumi.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import com.google.android.material.textfield.TextInputLayout
import net.noliaware.yumi.R
import net.noliaware.yumi.utils.*


class LoginView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var inputMessageTextView: TextView

    private lateinit var inputLayoutLogin: TextInputLayout
    private lateinit var inputLogin: EditText

    var callback: LoginViewCallback? by weak()

    interface LoginViewCallback {
        fun onLoginEntered(login: String)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {

        inputMessageTextView = findViewById(R.id.input_message_text_view)

        inputLayoutLogin = findViewById(R.id.input_layout_login)

        inputLogin = inputLayoutLogin.findViewById(R.id.input_login)
        inputLogin.addTextChangedListener(textWatcher)
        inputLogin.setOnEditorActionListener(onEditorActionListener)
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when {
                !inputLogin.text.isNullOrEmpty() -> {
                    inputLogin.error = null
                    inputLayoutLogin.isErrorEnabled = false
                }
            }
        }
    }

    private val onEditorActionListener = OnEditorActionListener { _, actionId, _ ->

        if (actionId == EditorInfo.IME_ACTION_DONE) {

            if (validateLogin()) {

                //confirmTextView.requestFocus()
                context.hideKeyboard()

                callback?.onLoginEntered(inputLogin.text.toString().trim())
            }
        }

        false
    }

    private fun validateLogin(): Boolean {

        val login = inputLogin.text.toString().trim()

        if (login.isEmpty()) {

            inputLayoutLogin.error = context.getString(R.string.login_empty_error)
            return false

        } else {

            inputLayoutLogin.isErrorEnabled = false
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        inputMessageTextView.measureWrapContent()

        inputLayoutLogin.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 8 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        inputMessageTextView.layoutToTopLeft(
            (viewWidth - inputMessageTextView.measuredWidth) / 2,
            convertDpToPx(20)
        )

        inputLayoutLogin.layoutToTopLeft(
            (viewWidth - inputLayoutLogin.measuredWidth) / 2,
            inputMessageTextView.bottom + convertDpToPx(20)
        )
    }
}