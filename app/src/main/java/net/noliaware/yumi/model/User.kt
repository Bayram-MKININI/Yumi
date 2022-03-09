package net.noliaware.yumi.model

import org.json.JSONObject

data class User(
    var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var birthDate: String = "",
    var birthCity: String = "",
    var address1: String = "",
    var address2: String = "",
    var zipCode: String = "",
    var city: String = "",
    var phone1: String = "",
    var phone2: String = "",
    var job: String = ""
) : JSONParcelable {

    override fun fillObjectFromJSON(jsonObject: JSONObject) {
        id = jsonObject.optString("user_id")
        firstName = jsonObject.optString("first_name")
        lastName = jsonObject.optString("last_name")
        birthDate = jsonObject.optString("birth_date")
        birthCity = jsonObject.optString("birth_city")
        address1 = jsonObject.optString("address_1")
        address2 = jsonObject.optString("address_2")
        zipCode = jsonObject.optString("zip_code")
        city = jsonObject.optString("city")
        phone1 = jsonObject.optString("phone_1")
        phone2 = jsonObject.optString("phone_2")
        job = jsonObject.optString("job")
    }

    override fun parseToJSON(): JSONObject {
        return JSONObject()
    }
}