package net.noliaware.yumi.commun.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ErrorDTO(
    @Json(name = "errorCode")
    val errorCode: String = "",
    @Json(name = "errorMessage")
    val errorMessage: String = "",
)