package net.noliaware.yumi.utils

import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkError
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.toolbox.StringRequest
import net.noliaware.yumi.model.DataManager
import net.noliaware.yumi.model.TOKEN
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection

data class ErrorResponse(
    var errorCode: Int = -99,
    var message: String = "",
    var retries: Int = -1,
)

class WebserviceProxy(
    val urlString: String = "",
    val parameters: HashMap<String, String> = hashMapOf(),
    val onJSONObjectReturned: (responseJsonObject: JSONObject) -> Unit,
    val onServiceError: (errorResponse: ErrorResponse) -> Unit
) {

    private val dataManager = DataManager.get()

    fun call() {

        val request: StringRequest = object : StringRequest(Method.POST, urlString,
            { responseStr ->

                try {

                    Log.e("WEBSERVICE", "Url =$urlString Response $responseStr")

                    val response = JSONObject(responseStr)

                    val status = response.optInt("status", 0)

                    val dataJsonObject = response.optJSONObject("data")

                    dataJsonObject?.let {

                        if (status == 1) {

                            val serviceError = ServiceError.fromString(it.optString("error_code", ""))
                            val errorMessage = it.optString("error_message", "")
                            val retries = it.optInt("remaining_attempts", -1)

                            onServiceError(
                                ErrorResponse(
                                    serviceError.errorCode,
                                    errorMessage,
                                    retries
                                )
                            )

                        } else {

                            dataManager.token = it.optString(TOKEN)
                            onJSONObjectReturned(it)
                        }
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { volleyError ->

                var serviceError: ServiceError = ServiceError.ErrSYSTEM

                if (volleyError is TimeoutError || volleyError is NetworkError)
                    serviceError = ServiceError.ErrNETWORK
                else if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == HttpURLConnection.HTTP_INTERNAL_ERROR)
                    serviceError = ServiceError.ErrSYSTEM

                Log.e("Error", volleyError.message ?: "")

                onServiceError(ErrorResponse(serviceError.errorCode))
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/x-www-form-urlencoded"
                return params
            }

            override fun getParams(): MutableMap<String, String> {
                addStandardPropertiesToMap()
                Log.e("WEBSERVICE", "Url =$urlString Parameters $parameters")
                return parameters
            }
        }

        request.retryPolicy = DefaultRetryPolicy(0, -1, 0F)
        request.tag = urlString
        dataManager.requestQueue.add(request)
    }

    private fun addStandardPropertiesToMap() {
        parameters.apply {

            put("login", dataManager.login)
            put("device_id", dataManager.getAndroidId())
            //put("app_version", BuildConfig.VERSION_NAME)

            if (dataManager.sessionId.isNotEmpty())
                put("session_id", dataManager.sessionId)

            if (dataManager.token.isNotEmpty())
                put("token", dataManager.token)
        }
    }
}
