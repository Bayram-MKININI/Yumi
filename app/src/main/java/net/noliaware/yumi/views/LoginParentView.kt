package net.noliaware.yumi.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ViewAnimator
import net.noliaware.yumi.R
import net.noliaware.yumi.utils.layoutToBottomLeft
import net.noliaware.yumi.utils.layoutToTopLeft

class LoginParentView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var backgroundView: ImageView
    private lateinit var loginViewAnimator: ViewAnimator

    lateinit var loginView: LoginView
        private set

    lateinit var passwordView: PasswordView
        private set

    private lateinit var rightViewOut: Animation
    private lateinit var rightViewIn: Animation

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        backgroundView = findViewById(R.id.background_view)
        loginViewAnimator = findViewById(R.id.login_view_animator)
        loginView = loginViewAnimator.findViewById(R.id.login_view)
        passwordView = loginViewAnimator.findViewById(R.id.password_view)

        rightViewIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        rightViewOut = AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
    }

    fun displayPasswordView() {
        loginViewAnimator.inAnimation = rightViewIn
        loginViewAnimator.outAnimation = rightViewOut
        loginViewAnimator.showNext()
        passwordView.fillPadViewWithData(intArrayOf(4, 0, 1, 5, 7, 8, 6, 3, 9, 2))
    }

    fun fillPadViewWithData(padDigitsArray: IntArray) {
        passwordView.fillPadViewWithData(padDigitsArray)
    }

    fun fillSecretDigitAtIndex(index: Int) {
        passwordView.fillSecretDigitAtIndex(index)
    }

    fun clearSecretDigits() {
        passwordView.clearSecretDigits()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        backgroundView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 3 / 2, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight / 2, MeasureSpec.EXACTLY)
        )

        loginViewAnimator.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight * 75 / 100, MeasureSpec.EXACTLY)
        )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        backgroundView.layoutToTopLeft(
            -backgroundView.measuredWidth * 3 / 10,
            -backgroundView.measuredHeight * 5 / 10
        )

        loginViewAnimator.layoutToBottomLeft(
            0,
            bottom
        )
    }
}