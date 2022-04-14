package net.noliaware.yumi.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ErrorDTO(
    @Json(name = "errorCode")
    val errorCode: Int = -1,
    @Json(name = "errorMessage")
    val errorMessage: String = "",
)