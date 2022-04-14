package net.noliaware.yumi.data.repository

import android.os.Build
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi.BuildConfig
import net.noliaware.yumi.commun.*
import net.noliaware.yumi.commun.util.DataError
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.commun.util.sha256
import net.noliaware.yumi.data.remote.RemoteApi
import net.noliaware.yumi.domain.model.ConnectData
import net.noliaware.yumi.domain.model.InitData
import net.noliaware.yumi.domain.model.SessionData
import net.noliaware.yumi.domain.model.Voucher
import okio.IOException
import retrofit2.HttpException
import java.util.*

class RepositoryImpl(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : Repository {

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
                    token = generateToken(timestamp, "init", randomString),
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

    private fun generateToken(timestamp: String, methodName: String, randomString: String): String {
        return "noliaware|$timestamp|${methodName}|${timestamp.reversed()}|$randomString".sha256()
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

    override fun getConnectData(password: String): Flow<Resource<ConnectData>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData =
                api.fetchConnectData(
                    timestamp = timestamp,
                    saltString = randomString,
                    token = generateToken(timestamp, "connect", randomString),
                    params = generateConnectParams(password)
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

                remoteData.data?.let { connectDTO ->
                    emit(Resource.Success(data = connectDTO.toConnectData()))
                }
            }

        } catch (ex: HttpException) {

            emit(Resource.Error(dataError = DataError.SYSTEM_ERROR))

        } catch (ex: IOException) {

            emit(Resource.Error(dataError = DataError.NETWORK_ERROR))
        }
    }

    private fun generateConnectParams(password: String) = mapOf(
        LOGIN to sessionData.login,
        APP_VERSION to BuildConfig.VERSION_NAME,
        DEVICE_ID to sessionData.deviceId,
        SESSION_ID to sessionData.sessionId,
        SESSION_TOKEN to sessionData.sessionToken,
        "password" to password
    )

    override fun getVoucherList(categoryId: String): Flow<Resource<List<Voucher>>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            Log.e("parameters", generateVoucherListParams(categoryId).toString())

            val remoteData =
                api.fetchVouchersByCategory(
                    timestamp = timestamp,
                    saltString = randomString,
                    token = generateToken(
                        timestamp,
                        "getAvailableVoucherListByCategory",
                        randomString
                    ),
                    params = generateVoucherListParams(categoryId)
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

                remoteData.data?.let { voucherDTOs ->
                    emit(Resource.Success(data = voucherDTOs.map { it.toVoucher() }))
                }
            }

        } catch (ex: HttpException) {

            emit(Resource.Error(dataError = DataError.SYSTEM_ERROR))

        } catch (ex: IOException) {

            emit(Resource.Error(dataError = DataError.NETWORK_ERROR))
        }
    }

    private fun generateVoucherListParams(categoryId: String) = mapOf(
        LOGIN to sessionData.login,
        APP_VERSION to BuildConfig.VERSION_NAME,
        DEVICE_ID to sessionData.deviceId,
        SESSION_ID to sessionData.sessionId,
        SESSION_TOKEN to sessionData.sessionToken,
        CATEGORY_ID to categoryId
    )
}