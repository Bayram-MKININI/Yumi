package net.noliaware.yumi.feature_alerts.data.repository

import android.util.Log
import net.noliaware.yumi.commun.GET_ALERT_LIST
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_alerts.domain.model.Alert
import okio.IOException
import retrofit2.HttpException
import java.util.*

class AlertsRepositoryImpl(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : AlertsRepository {

    override suspend fun getAlertList(): Resource<List<Alert>> {

        var errorType: ErrorType = ErrorType.SYSTEM_ERROR

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchAlertList(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    GET_ALERT_LIST,
                    randomString
                ),
                params = getCommonWSParams(sessionData)
            )

            handleSessionAndFailureIfAny(
                remoteData.session,
                sessionData,
                remoteData.error
            )?.let { errorMessage ->
                return Resource.Error(ErrorType.RECOVERABLE_ERROR, errorMessage = errorMessage)
            }

            remoteData.data?.let { alertsDTO ->
                Log.e("getAlerts", "Terminated")
                return Resource.Success(data = alertsDTO.alertDTOList.map { it.toAlert() })
            }

        } catch (ex: HttpException) {
            errorType = ErrorType.SYSTEM_ERROR
        } catch (ex: IOException) {
            errorType = ErrorType.NETWORK_ERROR
        }

        return Resource.Error(errorType = errorType)
    }
}