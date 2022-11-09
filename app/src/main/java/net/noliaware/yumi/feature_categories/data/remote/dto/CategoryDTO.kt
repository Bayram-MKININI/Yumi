package net.noliaware.yumi.feature_categories.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_categories.domain.model.Category

@JsonClass(generateAdapter = true)
data class CategoryDTO(
    @Json(name = "categoryId")
    val categoryId: String,
    @Json(name = "categoryLabel")
    val categoryLabel: String,
    @Json(name = "categoryDescription")
    val categoryDescription: String,
    @Json(name = "categoryColor")
    val categoryColor: String,
    @Json(name = "categoryIcon")
    val categoryIcon: String?,
    @Json(name = "availableVoucherCount")
    val availableVoucherCount: Int?,
    @Json(name = "usedVoucherCount")
    val usedVoucherCount: Int?
) {
    fun toCategory() = Category(
        categoryId = categoryId,
        categoryColor = categoryColor,
        categoryIcon = categoryIcon,
        categoryLabel = categoryLabel,
        categoryDescription = categoryDescription,
        availableVoucherCount = availableVoucherCount,
        usedVoucherCount = usedVoucherCount
    )
}