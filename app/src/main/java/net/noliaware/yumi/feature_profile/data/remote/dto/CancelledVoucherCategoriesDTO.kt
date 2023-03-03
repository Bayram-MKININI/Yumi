package net.noliaware.yumi.feature_profile.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_categories.data.remote.dto.CategoryDTO

@JsonClass(generateAdapter = true)
data class CancelledVoucherCategoriesDTO(
    @Json(name = "canceledVoucherDataPerCategory")
    val categoryDTOs: List<CategoryDTO>?
)