package net.noliaware.yumi.feature_categories.presentation.views

import java.io.Serializable

data class CategoryUI(
    val categoryColor: Int?,
    val categoryIcon: String?,
    val categoryLabel: String?
) : Serializable