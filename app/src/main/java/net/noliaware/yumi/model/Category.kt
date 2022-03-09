package net.noliaware.yumi.model

import org.json.JSONObject

data class Category(
    var categoryId: String = "",
    var categoryName: String = "",
    val vouchersList: MutableList<Voucher> = mutableListOf()
) : JSONParcelable {
    override fun fillObjectFromJSON(jsonObject: JSONObject) {

        categoryId = jsonObject.optString("categoryId")

        categoryName = jsonObject.optString("categoryName")

        jsonObject.optJSONArray("vouchers")?.let { vouchersJsonArray ->

            vouchersList.clear()

            for (i in 0 until vouchersJsonArray.length()) {

                val voucher = Voucher()
                voucher.fillObjectFromJSON(vouchersJsonArray.getJSONObject(i))
                vouchersList.add(voucher)
            }
        }
    }

    override fun parseToJSON(): JSONObject {
        return JSONObject()
    }
}