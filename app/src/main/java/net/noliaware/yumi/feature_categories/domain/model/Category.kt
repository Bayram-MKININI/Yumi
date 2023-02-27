package net.noliaware.yumi.feature_categories.domain.model

import java.io.Serializable

data class Category(
    val categoryId: String,
    val categoryColor: Int,
    val categoryIcon: String?,
    val categoryLabel: String,
    val categoryShortLabel: String,
    val categoryDescription: String,
    val availableVoucherCount: Int,
    val usedVoucherCount: Int
) : Serializable