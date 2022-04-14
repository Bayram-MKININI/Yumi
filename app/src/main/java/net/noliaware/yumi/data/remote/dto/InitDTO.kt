package net.noliaware.yumi.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.domain.model.InitData

@JsonClass(generateAdapter = true)
class InitDTO(
    @Json(name = "deviceId")
    val deviceId: String = "",
    @Json(name = "keyboard")
    val keyboard: List<Int> = listOf()
) {
    fun toInitData() = InitData(
        deviceId = deviceId,
        keyboard = keyboard
    )
}