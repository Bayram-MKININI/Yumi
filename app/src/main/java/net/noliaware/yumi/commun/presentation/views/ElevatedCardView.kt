package net.noliaware.yumi.commun.presentation.views

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.convertDpToPx

open class ElevatedCardView(
    context: Context,
    attrs: AttributeSet?
) : MaterialCardView(context, attrs) {
    override fun onFinishInflate() {
        super.onFinishInflate()
        cardElevation = convertDpToPx(16) * 1f
        setBackgroundResource(R.drawable.rectangle_rounded_white)
    }
}