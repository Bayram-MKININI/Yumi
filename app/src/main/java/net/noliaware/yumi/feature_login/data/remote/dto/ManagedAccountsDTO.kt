package net.noliaware.yumi.feature_login.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_profile.data.remote.dto.UserProfileDTO

@JsonClass(generateAdapter = true)
data class ManagedAccountsDTO(
    @Json(name = "managedAccounts")
    val managedAccountProfileDTOS: List<UserProfileDTO> = listOf()
)