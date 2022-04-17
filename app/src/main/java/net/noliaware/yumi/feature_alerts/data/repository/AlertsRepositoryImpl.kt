package net.noliaware.yumi.feature_alerts.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi.BuildConfig
import net.noliaware.yumi.commun.*
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.DataError
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.commun.util.generateToken
import net.noliaware.yumi.feature_alerts.domain.model.Alert
import okio.IOException
import retrofit2.HttpException
import java.util.*

class AlertsRepositoryImpl(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : AlertsRepository {

    override fun getAlertList(): Flow<Resource<List<Alert>>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            Log.e("parameters", generateAlertListParams().toString())

            val remoteData =
                api.fetchAlertList(
                    timestamp = timestamp,
                    saltString = randomString,
                    token = generateToken(
                        timestamp,
                        "getAlertList",
                        randomString
                    ),
                    params = generateAlertListParams()
                )

            remoteData.error?.let { errorDTO ->

                emit(
                    Resource.Error(
                        dataError = DataError.SYSTEM_ERROR,
                        errorCode = errorDTO.errorCode
                    )
                )

            } ?: run {

                remoteData.session?.let { sessionDTO ->
                    sessionData.apply {
                        sessionId = sessionDTO.sessionId
                        sessionToken = sessionDTO.sessionToken
                    }
                }

                remoteData.data?.let { alertsDTO ->
                    emit(Resource.Success(data = alertsDTO.alertDTOList.map { it.toAlert() }))
                }
            }

        } catch (ex: HttpException) {

            emit(Resource.Error(dataError = DataError.SYSTEM_ERROR))

        } catch (ex: IOException) {

            emit(Resource.Error(dataError = DataError.NETWORK_ERROR))
        }
    }

    private fun generateAlertListParams() = mapOf(
        LOGIN to sessionData.login,
        APP_VERSION to BuildConfig.VERSION_NAME,
        DEVICE_ID to sessionData.deviceId,
        SESSION_ID to sessionData.sessionId,
        SESSION_TOKEN to sessionData.sessionToken,
    )
}