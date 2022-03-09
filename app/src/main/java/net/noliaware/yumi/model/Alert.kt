package net.noliaware.yumi.model

import org.json.JSONObject

data class Alert(
    var id: String = "",
    var from: String = "",
    var timestamp: Long = 0,
    var text: String = ""
) : JSONParcelable {

    override fun fillObjectFromJSON(jsonObject: JSONObject) {
        id = jsonObject.optString("alert_id")
        from = jsonObject.optString("alert_from")
        timestamp = jsonObject.optLong("alert_datetime")
        text = jsonObject.optString("alert_text")
    }

    override fun parseToJSON(): JSONObject {
        return JSONObject()
    }
}