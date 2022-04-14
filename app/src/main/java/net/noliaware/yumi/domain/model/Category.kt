package net.noliaware.yumi.domain.model

import java.io.Serializable

data class Category(
    val categoryId: String,
    val categoryColor: String,
    val categoryIcon: String?,
    val categoryLabel: String,
    val voucherCount: Int
) : Serializable