package net.noliaware.yumi.controllers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import net.noliaware.yumi.model.*
import net.noliaware.yumi.utils.ErrorResponse
import net.noliaware.yumi.utils.WebserviceProxy
import org.json.JSONArray
import org.json.JSONObject

class LoginFragmentViewModel(application: Application) : AndroidViewModel(application) {

    val initLoaded = MutableLiveData<Boolean>()
    val errorResponseLiveData = MutableLiveData<ErrorResponse>()
    val connectLoaded = MutableLiveData<Boolean>()

    fun callInitWebservice() {
        WebserviceProxy(
            urlString = INIT_DATA_URL,
            onJSONObjectReturned = { responseJsonObject ->
                handleInitResponse(responseJsonObject)
                initLoaded.postValue(true)
            },
            onServiceError = { errorResponse ->
                errorResponseLiveData.postValue(errorResponse)
            }
        ).call()
    }

    private fun handleInitResponse(responseJsonObject: JSONObject) {

        val dataManager = DataManager.get()

        dataManager.sessionId = responseJsonObject.optString(SESSION_ID)

        val keyboardJsonArray = JSONArray(responseJsonObject.optString(KEYBOARD))

        keyboardJsonArray.let {

            for (i in 0 until it.length()) {
                dataManager.keyboardSymbols[i] = it.getInt(i)
            }
        }
    }

    fun callConnectWebserviceWithIndexes(indexes: List<Int>) {

        val parameters = hashMapOf<String, String>()
        val passwordJson = JSONArray()

        indexes.forEach {
            passwordJson.put(it)
        }

        parameters["password"] = passwordJson.toString()

        WebserviceProxy(
            urlString = CONNECT_URL,
            parameters = parameters,
            onJSONObjectReturned = { responseJsonObject ->
                handleConnectResponse(responseJsonObject)
                connectLoaded.postValue(true)
            },
            onServiceError = { errorResponse ->
                errorResponseLiveData.postValue(errorResponse)
            }
        ).call()
    }

    private fun handleConnectResponse(responseJsonObject: JSONObject) {

        val dataManager = DataManager.get()

        dataManager.encryptionVector = responseJsonObject.optString(ENCRYPTION_VECTOR)

        responseJsonObject.optJSONArray("categories")?.let { jsonArray ->

            if (dataManager.categoriesList.isNotEmpty())
                dataManager.categoriesList.clear()

            for (i in 0 until jsonArray.length()) {

                Category().also {
                    it.fillObjectFromJSON(jsonArray.getJSONObject(i))
                    dataManager.categoriesList.add(it)
                }
            }
        }
    }
}