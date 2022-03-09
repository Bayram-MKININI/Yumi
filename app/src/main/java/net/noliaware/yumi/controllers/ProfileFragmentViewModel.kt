package net.noliaware.yumi.controllers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import net.noliaware.yumi.model.Category
import net.noliaware.yumi.model.DataManager
import net.noliaware.yumi.model.GET_INFO_URL
import net.noliaware.yumi.utils.ErrorResponse
import net.noliaware.yumi.utils.SingleLiveEvent
import net.noliaware.yumi.utils.WebserviceProxy
import org.json.JSONObject

class ProfileFragmentViewModel(application: Application) : AndroidViewModel(application) {

    val dataLoaded = SingleLiveEvent<Boolean>()
    val errorResponseLiveData = SingleLiveEvent<ErrorResponse>()
    val consumedCategoriesList = mutableListOf<Category>()

    fun callGetInfoWebservice() {

        WebserviceProxy(
            urlString = GET_INFO_URL,
            onJSONObjectReturned = { responseJsonObject ->
                handleGetInfoResponse(responseJsonObject)
                dataLoaded.value = true
            },
            onServiceError = { errorResponse ->
                errorResponseLiveData.value = errorResponse
            }
        ).call()
    }

    private fun handleGetInfoResponse(responseJsonObject: JSONObject) {

        responseJsonObject.optJSONObject("infos")?.let { infoJsonObject ->

            val dataManager = DataManager.get();

            dataManager.user.fillObjectFromJSON(infoJsonObject.getJSONObject("user"))

            responseJsonObject.optJSONArray("categories")?.let { jsonArray ->

                if (consumedCategoriesList.isNotEmpty())
                    consumedCategoriesList.clear()

                for (i in 0 until jsonArray.length()) {

                    Category().also {
                        it.fillObjectFromJSON(jsonArray.getJSONObject(i))
                        consumedCategoriesList.add(it)
                    }
                }
            }
        }
    }
}