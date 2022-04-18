package net.noliaware.yumi.feature_login.data.repository

import android.os.Build
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi.BuildConfig
import net.noliaware.yumi.commun.*
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.DataError
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.commun.util.generateToken
import net.noliaware.yumi.commun.util.getCommunWSParams
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_login.domain.model.InitData
import okio.IOException
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : LoginRepository {

    override fun getInitData(
        androidId: String,
        deviceId: String?,
        login: String
    ): Flow<Resource<InitData>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData =
                api.fetchInitData(
                    timestamp = timestamp,
                    saltString = randomString,
                    token = generateToken(timestamp, INIT, randomString),
                    params = generateInitParams(androidId, deviceId, login)
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
                        this.login = login
                        this.sessionId = sessionDTO.sessionId
                        this.sessionToken = sessionDTO.sessionToken
                    }
                }

                remoteData.data?.let { initDTO ->
                    sessionData.deviceId = initDTO.deviceId
                    emit(Resource.Success(data = initDTO.toInitData()))
                }
            }

        } catch (ex: HttpException) {
            emit(Resource.Error(dataError = DataError.SYSTEM_ERROR))
        } catch (ex: IOException) {
            emit(Resource.Error(dataError = DataError.NETWORK_ERROR))
        }
    }

    private fun generateInitParams(
        androidId: String,
        deviceId: String?,
        login: String
    ): Map<String, String> {

        val parameters = mutableMapOf(
            LOGIN to login,
            APP_VERSION to BuildConfig.VERSION_NAME
        )

        deviceId?.let {
            parameters[DEVICE_ID] = it
        } ?: run {
            parameters["deviceType"] = "S"
            parameters["deviceOS"] = "ANDROID"
            parameters["deviceUUID"] = androidId
            parameters["deviceLabel"] = Build.MODEL
        }

        return parameters
    }

    override fun getAccountData(password: String): Flow<Resource<AccountData>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData =
                api.fetchAccountDataForPassword(
                    timestamp = timestamp,
                    saltString = randomString,
                    token = generateToken(timestamp, CONNECT, randomString),
                    params = generateGetAccountParams(password)
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

                remoteData.data?.let { accountDataDTO ->
                    emit(Resource.Success(data = accountDataDTO.toAccountData()))
                }
            }

        } catch (ex: HttpException) {

            emit(Resource.Error(dataError = DataError.SYSTEM_ERROR))

        } catch (ex: IOException) {

            emit(Resource.Error(dataError = DataError.NETWORK_ERROR))
        }
    }

    private fun generateGetAccountParams(password: String) = mutableMapOf(
        "password" to password
    ).also { it.plusAssign(getCommunWSParams(sessionData)) }

    override fun selectAccountForId(accountId: String): Flow<Resource<AccountData>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData =
                api.fetchSelectAccountForId(
                    timestamp = timestamp,
                    saltString = randomString,
                    token = generateToken(timestamp, SELECT_ACCOUNT, randomString),
                    params = generateSelectAccountParams(accountId)
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

                remoteData.data?.let { accountDataDTO ->
                    emit(Resource.Success(data = accountDataDTO.toAccountData()))
                }
            }

        } catch (ex: HttpException) {

            emit(Resource.Error(dataError = DataError.SYSTEM_ERROR))

        } catch (ex: IOException) {

            emit(Resource.Error(dataError = DataError.NETWORK_ERROR))
        }
    }

    private fun generateSelectAccountParams(accountId: String) = mutableMapOf(
        "managedAccount" to accountId
    ).also { it.plusAssign(getCommunWSParams(sessionData)) }
}