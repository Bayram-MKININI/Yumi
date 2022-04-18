package net.noliaware.yumi.feature_message.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_message.domain.model.Message

@JsonClass(generateAdapter = true)
data class SingleMessageDTO(
    @Json(name = "message")
    val message: Message
)