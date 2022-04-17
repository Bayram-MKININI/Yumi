package net.noliaware.yumi.feature_profile.domain.model

import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_categories.domain.model.Category
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class AccountProfile(
    val login: String,
    val title: String,
    val firstName: String,
    val lastName: String,
    val cellNumber: String? = null,
    val availableVoucherCount: Int,
    val categories: List<Category>
) : Serializable