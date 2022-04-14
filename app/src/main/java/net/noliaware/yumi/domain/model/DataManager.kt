package net.noliaware.yumi.domain.model

import android.content.Context
import android.provider.Settings
import android.text.TextUtils

class DataManager private constructor(private val context: Context) {

    var login: String = ""
    var sessionId: String = ""
    var sessionToken: String = ""
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
}