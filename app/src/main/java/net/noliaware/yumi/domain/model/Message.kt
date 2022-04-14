package net.noliaware.yumi.domain.model

import org.json.JSONObject

data class Message(
    var id: String = "",
    var from: String = "",
    var to: String = "",
    var timestamp: Long = 0,
    var subject: String = "",
    var text: String = ""
) : JSONParcelable {

    override fun fillObjectFromJSON(jsonObject: JSONObject) {
        id = jsonObject.optString("message_id")
        from = jsonObject.optString("message_from")
        to = jsonObject.optString("message_to")
        timestamp = jsonObject.optLong("message_datetime")
        subject = jsonObject.optString("message_subject")
        text = jsonObject.optString("message_text")
    }

    override fun parseToJSON(): JSONObject {
        return JSONObject().let {
            it.put("message_id", id)
            it.put("message_from", from)
            it.put("message_to", to)
            it.put("message_datetime", timestamp)
            it.put("message_subject", subject)
            it.put("message_text", text)
        }
    }
}