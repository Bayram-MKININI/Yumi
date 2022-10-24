package net.noliaware.yumi.commun.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ResponseDTO<T>(
    @Json(name = "status")
    val status: Int,
    //@Json(name = "actions")
    //val actions: List<String> = listOf(),
    @Json(name = "session")
    val session: SessionDTO?,
    @Json(name = "error")
    val error: ErrorDTO?,
    @Json(name = "data")
    val data: T?
)