package net.noliaware.yumi.feature_categories.presentation.views

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryUI(
    val categoryColor: Int,
    val categoryIcon: String?,
    val categoryLabel: String
) : Parcelable