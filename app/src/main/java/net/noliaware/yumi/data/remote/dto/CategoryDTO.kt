package net.noliaware.yumi.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.domain.model.Category
import net.noliaware.yumi.domain.model.MessageSubject

@JsonClass(generateAdapter = true)
data class CategoryDTO(
    @Json(name = "categorycolor")
    val categoryColor: String,
    @Json(name = "categoryicon")
    val categoryIcon: String?,
    @Json(name = "categoryid")
    val categoryId: String,
    @Json(name = "categorylabel")
    val categoryLabel: String,
    @Json(name = "vouchercount")
    val voucherCount: Int
) {
    fun toCategory() = Category(
        categoryId = categoryId,
        categoryColor = categoryColor,
        categoryIcon = categoryIcon,
        categoryLabel = categoryLabel,
        voucherCount = voucherCount,
    )
}