package net.noliaware.yumi.model

import org.json.JSONObject

data class Voucher(
    var id: String = "",
    var title: String = "",
    var url: String = "",
    var price: String = "",
    var description: String = "",
) : JSONParcelable {

    override fun fillObjectFromJSON(jsonObject: JSONObject) {
        id = jsonObject.optString("voucher_id")
        title = jsonObject.optString("title")
        url = jsonObject.optString("url")
        price = jsonObject.optString("price")
        description = jsonObject.optString("description")
    }

    override fun parseToJSON(): JSONObject {
        return JSONObject()
    }
}