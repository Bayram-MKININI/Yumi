package net.noliaware.yumi.feature_categories.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi.commun.ApiConstants.GET_AVAILABLE_DATA_PER_CATEGORY
import net.noliaware.yumi.commun.ApiConstants.GET_CANCELLED_DATA_PER_CATEGORY
import net.noliaware.yumi.commun.ApiConstants.GET_USED_DATA_PER_CATEGORY
import net.noliaware.yumi.commun.ApiConstants.GET_VOUCHER
import net.noliaware.yumi.commun.ApiConstants.GET_VOUCHER_STATUS
import net.noliaware.yumi.commun.ApiConstants.SET_PRIVACY_POLICY_READ_STATUS
import net.noliaware.yumi.commun.ApiConstants.USE_VOUCHER
import net.noliaware.yumi.commun.ApiParameters.LIST_PAGE_SIZE
import net.noliaware.yumi.commun.ApiParameters.VOUCHER_ID
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.ErrorType
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.commun.util.generateToken
import net.noliaware.yumi.commun.util.getCommonWSParams
import net.noliaware.yumi.commun.util.handleSessionWithNoFailure
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_categories.domain.model.VoucherStateData
import net.noliaware.yumi.feature_categories.domain.repository.CategoryRepository
import okio.IOException
import retrofit2.HttpException
import java.util.UUID
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : CategoryRepository {

    override fun updatePrivacyPolicyReadStatus(): Flow<Resource<Boolean>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.updatePrivacyPolicyReadStatus(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = SET_PRIVACY_POLICY_READ_STATUS,
                    randomString = randomString
                ),
                params = getCommonWSParams(sessionData, SET_PRIVACY_POLICY_READ_STATUS)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = SET_PRIVACY_POLICY_READ_STATUS,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                emit(
                    Resource.Success(
                        data = remoteData.data?.result == 1,
                        appMessage = remoteData.message?.toAppMessage()
                    )
                )
            }

        } catch (ex: HttpException) {
            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))
        } catch (ex: IOException) {
            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
        }
    }

    override fun getAvailableCategories(): Flow<Resource<List<Category>>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchAvailableDataByCategory(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_AVAILABLE_DATA_PER_CATEGORY,
                    randomString = randomString
                ),
                params = getCommonWSParams(sessionData, GET_AVAILABLE_DATA_PER_CATEGORY)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_AVAILABLE_DATA_PER_CATEGORY,
                appMessage = remoteData.message,
                error = remoteData.error
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

    override fun getAvailableVoucherList(categoryId: String) = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        VoucherPagingSource(api, sessionData, categoryId)
    }.flow

    override fun getUsedCategories(): Flow<Resource<List<Category>>> = flow {

        emit(Resource.Loading())

        try {
            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchUsedDataByCategory(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_USED_DATA_PER_CATEGORY,
                    randomString = randomString
                ),
                params = getCommonWSParams(sessionData, GET_USED_DATA_PER_CATEGORY)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_USED_DATA_PER_CATEGORY,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.categoryDTOs?.let { categoriesDTO ->
                    emit(
                        Resource.Success(
                            data = categoriesDTO.map { it.toCategory() },
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

    override fun getUsedVoucherList(categoryId: String) = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        UsedVoucherPagingSource(api, sessionData, categoryId)
    }.flow

    override fun getCancelledCategories(): Flow<Resource<List<Category>>> = flow {

        emit(Resource.Loading())

        try {
            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchCancelledDataByCategory(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_CANCELLED_DATA_PER_CATEGORY,
                    randomString = randomString
                ),
                params = getCommonWSParams(sessionData, GET_CANCELLED_DATA_PER_CATEGORY)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_CANCELLED_DATA_PER_CATEGORY,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.categoryDTOs?.let { categoriesDTO ->
                    emit(
                        Resource.Success(
                            data = categoriesDTO.map { it.toCategory() },
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

    override fun getCancelledVoucherList(categoryId: String) = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        CancelledVoucherPagingSource(api, sessionData, categoryId)
    }.flow

    override fun getVoucherById(voucherId: String): Flow<Resource<Voucher>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchVoucherForId(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_VOUCHER,
                    randomString = randomString
                ),
                params = generateVoucherByIdParams(voucherId, GET_VOUCHER)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_VOUCHER,
                appMessage = remoteData.message,
                error = remoteData.error
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

    override fun getVoucherStateDataById(voucherId: String): Flow<Resource<VoucherStateData>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchVoucherStatusForId(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_VOUCHER_STATUS,
                    randomString = randomString
                ),
                params = generateVoucherByIdParams(voucherId, GET_VOUCHER_STATUS)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_VOUCHER_STATUS,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.let { getVoucherStateDataDTO ->
                    emit(
                        Resource.Success(
                            data = getVoucherStateDataDTO.voucherStateData.toVoucherStateData(),
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

    override fun useVoucherById(voucherId: String): Flow<Resource<Boolean>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.useVoucher(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    USE_VOUCHER,
                    randomString
                ),
                params = generateVoucherByIdParams(voucherId, USE_VOUCHER)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = USE_VOUCHER,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                emit(
                    Resource.Success(
                        data = remoteData.data != null,
                        appMessage = remoteData.message?.toAppMessage()
                    )
                )
            }

        } catch (ex: HttpException) {
            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))
        } catch (ex: IOException) {
            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
        }
    }

    private fun generateVoucherByIdParams(voucherId: String, tokenKey: String) = mutableMapOf(
        VOUCHER_ID to voucherId
    ).also { it.plusAssign(getCommonWSParams(sessionData, tokenKey)) }
}