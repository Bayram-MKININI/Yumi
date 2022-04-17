package net.noliaware.yumi.feature_categories.data.repository

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
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import okio.IOException
import retrofit2.HttpException
import java.util.*

class CategoryRepositoryImpl(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : CategoryRepository {

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

                remoteData.data?.let { vouchersDTO ->
                    emit(Resource.Success(data = vouchersDTO.voucherDTOList.map { it.toVoucher() }))
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

    override fun getVoucherById(voucherId: String): Flow<Resource<Voucher>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData =
                api.fetchVoucherById(
                    timestamp = timestamp,
                    saltString = randomString,
                    token = generateToken(
                        timestamp,
                        GET_VOUCHER,
                        randomString
                    ),
                    params = generateVoucherByIdParams(voucherId)
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

                remoteData.data?.let { voucherDTO ->
                    emit(Resource.Success(data = voucherDTO.toVoucher() ))
                }
            }

        } catch (ex: HttpException) {

            emit(Resource.Error(dataError = DataError.SYSTEM_ERROR))

        } catch (ex: IOException) {

            emit(Resource.Error(dataError = DataError.NETWORK_ERROR))
        }
    }

    private fun generateVoucherByIdParams(voucherId: String) = mapOf(
        LOGIN to sessionData.login,
        APP_VERSION to BuildConfig.VERSION_NAME,
        DEVICE_ID to sessionData.deviceId,
        SESSION_ID to sessionData.sessionId,
        SESSION_TOKEN to sessionData.sessionToken,
        VOUCHER_ID to voucherId
    )
}