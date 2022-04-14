package net.noliaware.yumi.presentation.controllers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import net.noliaware.yumi.domain.model.Category
import net.noliaware.yumi.domain.model.DataManager
import net.noliaware.yumi.commun.GET_INFO_URL
import org.json.JSONObject

class ProfileFragmentViewModel(application: Application) : AndroidViewModel(application) {

    //val dataLoaded = SingleLiveEvent<Boolean>()
    //val errorResponseLiveData = SingleLiveEvent<ErrorResponse>()
    val consumedCategoriesList = mutableListOf<Category>()

    fun callGetInfoWebservice() {

       /* WebserviceProxy(
            urlString = GET_INFO_URL,
            onJSONObjectReturned = { responseJsonObject ->
                handleGetInfoResponse(responseJsonObject)
                dataLoaded.value = true
            },
            onServiceError = { errorResponse ->
                errorResponseLiveData.value = errorResponse
            }
        ).call()

        */
    }
}