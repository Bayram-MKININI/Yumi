package net.noliaware.yumi.feature_categories.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val categoryId: String,
    val categoryColor: Int,
    val categoryIcon: String?,
    val categoryLabel: String,
    val categoryShortLabel: String,
    val categoryDescription: String,
    val availableVoucherCount: Int,
    val usedVoucherCount: Int,
    val cancelledVoucherCount: Int
) : Parcelable