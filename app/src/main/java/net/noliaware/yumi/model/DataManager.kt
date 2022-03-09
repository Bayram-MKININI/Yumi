package net.noliaware.yumi.model

import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class DataManager private constructor(private val context: Context) {

    val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    var login: String = ""
    var sessionId: String = ""
    var token: String = ""
    val keyboardSymbols = IntArray(10)
    var encryptionVector: String = ""
    var user: User = User()
    val categoriesList = mutableListOf<Category>()

    fun getAndroidId(): String = Settings.Secure.getString(
        context.applicationContext.contentResolver,
        Settings.Secure.ANDROID_ID
    )

    companion object {
        private var INSTANCE: DataManager? = null

        fun initialize(context: Context) {
            if (INSTANCE == null)
                INSTANCE = DataManager(context)
        }

        fun get(): DataManager {
            return INSTANCE ?: throw IllegalStateException("DataManager must be initialized")
        }
    }

    fun getCategoryById(categoryId: String?): Category? {

        for (category in categoriesList) {

            if (TextUtils.equals(category.categoryName, categoryId))
                return category
        }
        return null
    }

    fun getVoucherById(voucherId: String?): Voucher? {

        for (category in categoriesList) {

            for (voucher in category.vouchersList) {

                if (TextUtils.equals(voucher.id, voucherId))
                    return voucher
            }
        }
        return null
    }
}