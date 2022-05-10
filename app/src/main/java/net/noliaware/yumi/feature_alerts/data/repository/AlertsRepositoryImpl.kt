package net.noliaware.yumi.feature_alerts.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    override fun getAlertList(): Flow<Resource<List<Alert>>> = flow {

        emit(Resource.Loading())

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

            val sessionNoFailure =
                !handleSessionAndFailureIfAny(remoteData.session, sessionData, remoteData.error)

            if (sessionNoFailure) {
                remoteData.data?.let { alertsDTO ->
                    emit(Resource.Success(data = alertsDTO.alertDTOList.map { it.toAlert() }))
                }
            }

        } catch (ex: HttpException) {

            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))

        } catch (ex: IOException) {

            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
        }
    }
}