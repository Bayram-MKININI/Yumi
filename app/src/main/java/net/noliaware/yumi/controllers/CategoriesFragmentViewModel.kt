package net.noliaware.yumi.controllers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import net.noliaware.yumi.model.*
import net.noliaware.yumi.utils.ErrorResponse
import net.noliaware.yumi.utils.WebserviceProxy
import org.json.JSONObject

class CategoriesFragmentViewModel(application: Application) : AndroidViewModel(application) {

    val alertsList = mutableListOf<Alert>()
    val liveAlertsList = MutableLiveData<MutableList<Alert>>()
    val errorInboxResponseLiveData = MutableLiveData<ErrorResponse>()

    init {
        liveAlertsList.value = ArrayList()
    }

    fun callGetAlertsWebservice() {

        val parameters = hashMapOf<String, String>()
        parameters[LIMIT] = 0.toString()
        parameters[OFFSET] = 0.toString()

        WebserviceProxy(
            urlString = GET_ALERTS_URL,
            parameters = parameters,
            onJSONObjectReturned = { responseJsonObject ->
                handleAlertsResponse(responseJsonObject)
                liveAlertsList.postValue(alertsList)
            },
            onServiceError = { errorResponse ->
                errorInboxResponseLiveData.postValue(errorResponse)
            }
        ).call()
    }

    private fun handleAlertsResponse(responseJsonObject: JSONObject) {

        responseJsonObject.optJSONArray(ALERTS)?.let {

            alertsList.clear()

            for (i in 0 until it.length()) {
                val alert = Alert()
                alert.fillObjectFromJSON(it.getJSONObject(i))
                alertsList.add(alert)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        //cancel request
    }
}