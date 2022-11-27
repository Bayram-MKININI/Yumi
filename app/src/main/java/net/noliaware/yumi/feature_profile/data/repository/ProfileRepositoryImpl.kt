package net.noliaware.yumi.feature_profile.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi.commun.CATEGORY_ID
import net.noliaware.yumi.commun.GET_ACCOUNT
import net.noliaware.yumi.commun.GET_DATA_PER_CATEGORY
import net.noliaware.yumi.commun.GET_USED_VOUCHER_LIST_BY_CATEGORY
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_categories.domain.model.Category
import net.noliaware.yumi.feature_categories.domain.model.Voucher
import net.noliaware.yumi.feature_profile.domain.model.UserProfile
import okio.IOException
import retrofit2.HttpException
import java.util.*

class ProfileRepositoryImpl(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : ProfileRepository {

    override fun getUserProfile(): Flow<Resource<UserProfile>> = flow {

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchAccount(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    GET_ACCOUNT,
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

                remoteData.data?.userProfileDTO?.toUserProfile()?.let { userProfile ->

                    if (userProfile.usedVoucherCount > 0) {

                        when (val result = getUsedCategories()) {
                            is Resource.Error -> emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))
                            is Resource.Loading -> Unit
                            is Resource.Success -> {
                                result.data?.let { categories ->
                                    userProfile.categories = categories
                                }
                            }
                        }
                    }

                    emit(
                        Resource.Success(
                            data = userProfile,
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

    private suspend fun getUsedCategories(): Resource<List<Category>> {

        val timestamp = System.currentTimeMillis().toString()
        val randomString = UUID.randomUUID().toString()

        val remoteCategoriesData = api.fetchDataByCategory(
            timestamp = timestamp,
            saltString = randomString,
            token = generateToken(
                timestamp,
                GET_DATA_PER_CATEGORY,
                randomString
            ),
            params = getCommonWSParams(sessionData)
        )

        remoteCategoriesData.data?.let { voucherCategoriesDTO ->

            remoteCategoriesData.session?.let { sessionDTO ->
                sessionData.apply {
                    sessionId = sessionDTO.sessionId
                    sessionToken = sessionDTO.sessionToken
                }
            }

            voucherCategoriesDTO.categoryDTOs?.let { categoriesDTO ->
                return Resource.Success(data = categoriesDTO.map { it.toCategory() })
            }
        }

        return Resource.Error(errorType = ErrorType.SYSTEM_ERROR)
    }

    override fun getUsedVoucherList(categoryId: String): Flow<Resource<List<Voucher>>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchUsedVouchersForCategory(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    GET_USED_VOUCHER_LIST_BY_CATEGORY,
                    randomString
                ),
                params = generateUsedVoucherListParams(categoryId)
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

    private fun generateUsedVoucherListParams(categoryId: String) = mutableMapOf(
        CATEGORY_ID to categoryId
    ).also { it.plusAssign(getCommonWSParams(sessionData)) }
}