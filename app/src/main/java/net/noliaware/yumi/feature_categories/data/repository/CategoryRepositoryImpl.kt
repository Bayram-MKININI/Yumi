package net.noliaware.yumi.feature_categories.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi.commun.*
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.domain.model.VoucherStatus
import okio.IOException
import retrofit2.HttpException
import java.util.*

class CategoryRepositoryImpl(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : CategoryRepository {

    override fun getAvailableCategories(): Flow<Resource<List<Category>>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchDataByCategory(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    GET_DATA_PER_CATEGORY,
                    randomString
                ),
                params = getCommonWSParams(sessionData)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                remoteData.session,
                sessionData,
                remoteData.message,
                remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.let { voucherCategoriesDTO ->
                    voucherCategoriesDTO.categoryDTOs?.let { categoriesDTO ->
                        emit(
                            Resource.Success(
                                data = categoriesDTO.map { it.toCategory() },
                                appMessage = remoteData.message?.toAppMessage()
                            )
                        )
                    }
                }
            }

        } catch (ex: HttpException) {
            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))
        } catch (ex: IOException) {
            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
        }
    }

    override fun getVoucherList(categoryId: String): Flow<Resource<List<Voucher>>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchVouchersForCategory(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    GET_AVAILABLE_VOUCHER_LIST_BY_CATEGORY,
                    randomString
                ),
                params = generateVoucherListParams(categoryId)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                remoteData.session,
                sessionData,
                remoteData.message,
                remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.let { vouchersDTO ->
                    emit(
                        Resource.Success(
                            data = vouchersDTO.voucherDTOList.map { it.toVoucher() },
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

    private fun generateVoucherListParams(categoryId: String) = mutableMapOf(
        CATEGORY_ID to categoryId
    ).also { it.plusAssign(getCommonWSParams(sessionData)) }

    override fun getVoucherById(voucherId: String): Flow<Resource<Voucher>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchVoucherForId(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    GET_VOUCHER,
                    randomString
                ),
                params = generateVoucherByIdParams(voucherId)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                remoteData.session,
                sessionData,
                remoteData.message,
                remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.let { getVoucherDTO ->
                    emit(
                        Resource.Success(
                            data = getVoucherDTO.voucherDTO.toVoucher(sessionData.sessionId),
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

    override fun getVoucherStatusById(voucherId: String): Flow<Resource<VoucherStatus>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchVoucherStatusForId(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    GET_VOUCHER_STATUS,
                    randomString
                ),
                params = generateVoucherByIdParams(voucherId)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                remoteData.session,
                sessionData,
                remoteData.message,
                remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.let { getVoucherStatusDTO ->
                    emit(
                        Resource.Success(
                            data = getVoucherStatusDTO.toVoucherStatus(),
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

    private fun generateVoucherByIdParams(voucherId: String) = mutableMapOf(
        VOUCHER_ID to voucherId
    ).also { it.plusAssign(getCommonWSParams(sessionData)) }
}