package net.noliaware.yumi.feature_message.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessagesDTO(
    @Json(name = "messages")
    val messageDTOList: List<MessageDTO>
)