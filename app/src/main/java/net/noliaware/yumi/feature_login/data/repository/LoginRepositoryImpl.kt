package net.noliaware.yumi.feature_login.data.repository

import android.os.Build
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi.BuildConfig
import net.noliaware.yumi.commun.*
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_login.domain.model.InitData
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
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

            val remoteData = api.fetchInitData(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(timestamp, INIT, randomString),
                params = generateInitParams(androidId, deviceId, login)
            )

            remoteData.error?.let { errorDTO ->

                emit(
                    Resource.Error(
                        errorType = ErrorType.SYSTEM_ERROR,
                        errorMessage = errorDTO.errorMessage
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
            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))
        } catch (ex: IOException) {
            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
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

        if (deviceId.isNullOrEmpty()) {
            parameters[DEVICE_TYPE] = "S"
            parameters[DEVICE_OS] = "ANDROID"
            parameters[DEVICE_UUID] = androidId
            parameters[DEVICE_LABEL] = Build.MODEL
        } else {
            parameters[DEVICE_ID] = deviceId
        }

        return parameters
    }

    override fun getManagedProfileList(): Flow<PagingData<UserProfile>> = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        ManagedAccountPagingSource(api, sessionData)
    }.flow

    override fun getAccountData(password: String): Flow<Resource<AccountData>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchAccountDataForPassword(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(timestamp, CONNECT, randomString),
                params = generateGetAccountParams(password)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                remoteData.session,
                sessionData,
                remoteData.message,
                remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.let { accountDataDTO ->
                    emit(
                        Resource.Success(
                            data = accountDataDTO.toAccountData(),
                            appMessage = remoteData.message?.toAppMessage()
                        )
                    )
                }
            }

        } catch (ex: HttpException) {
            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))
        } catch (ex: IOException) {
            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
        }
    }

    private fun generateGetAccountParams(password: String) = mutableMapOf(
        PASSWORD to password
    ).also { it.plusAssign(getCommonWSParams(sessionData)) }

    override fun selectAccountForId(accountId: String): Flow<Resource<AccountData>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchSelectAccountForId(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(timestamp, SELECT_ACCOUNT, randomString),
                params = generateSelectAccountParams(accountId)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                remoteData.session,
                sessionData,
                remoteData.message,
                remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.let { accountDataDTO ->
                    emit(
                        Resource.Success(
                            data = accountDataDTO.toAccountData(),
                            appMessage = remoteData.message?.toAppMessage()
                        )
                    )
                }
            }

        } catch (ex: HttpException) {
            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))
        } catch (ex: IOException) {
            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
        }
    }

    private fun generateSelectAccountParams(accountId: String) = mutableMapOf(
        MANAGED_ACCOUNT to accountId
    ).also { it.plusAssign(getCommonWSParams(sessionData)) }
}