package net.noliaware.yumi.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.domain.model.MessageSubject

@JsonClass(generateAdapter = true)
data class MessageSubjectDTO(
    @Json(name = "subjectid")
    val subjectId: Int,
    @Json(name = "subjectlabel")
    val subjectLabel: String
) {
    fun toMessageSubject() = MessageSubject(
        subjectId = subjectId,
        subjectLabel = subjectLabel
    )
}