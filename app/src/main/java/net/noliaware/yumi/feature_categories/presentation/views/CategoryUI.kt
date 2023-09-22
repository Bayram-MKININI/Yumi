package net.noliaware.yumi.feature_categories.presentation.views

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CategoryUI(
    val categoryColor: Int,
    val categoryIcon: String?,
    val categoryLabel: String
) : Parcelable