package net.noliaware.yumi.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder<T> internal constructor(
    private val view: View,
    private val expression: (T, View) -> Unit
) : RecyclerView.ViewHolder(view) {
    fun bind(item: T) {
        expression(item, view)
    }
}