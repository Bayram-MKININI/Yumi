package net.noliaware.yumi.domain.model

import org.json.JSONException
import org.json.JSONObject

interface JSONParcelable {
    @Throws(JSONException::class)
    fun fillObjectFromJSON(jsonObject: JSONObject)

    fun parseToJSON(): JSONObject
}