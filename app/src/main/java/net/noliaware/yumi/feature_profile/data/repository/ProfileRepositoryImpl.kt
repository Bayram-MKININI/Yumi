package net.noliaware.yumi.feature_profile.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi.commun.GET_ACCOUNT
import net.noliaware.yumi.commun.GET_DATA_PER_CATEGORY
import net.noliaware.yumi.commun.LIST_PAGE_SIZE
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_categories.domain.model.Category
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
                    timestamp = timestamp,
                    methodName = GET_ACCOUNT,
                    randomString = randomString
                ),
                params = getCommonWSParams(sessionData, GET_ACCOUNT)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_ACCOUNT,
                appMessage = remoteData.message,
                error = remoteData.error
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
                timestamp = timestamp,
                methodName = GET_DATA_PER_CATEGORY,
                randomString = randomString
            ),
            params = getCommonWSParams(sessionData, GET_DATA_PER_CATEGORY)
        )

        remoteCategoriesData.data?.let { voucherCategoriesDTO ->

            remoteCategoriesData.session?.let { sessionDTO ->
                sessionData.apply {
                    sessionId = sessionDTO.sessionId
                    sessionTokens[GET_DATA_PER_CATEGORY] = sessionDTO.sessionToken
                }
            }

            voucherCategoriesDTO.categoryDTOs?.let { categoriesDTO ->
                return Resource.Success(data = categoriesDTO.map { it.toCategory() })
            }
        }

        return Resource.Error(errorType = ErrorType.SYSTEM_ERROR)
    }

    override fun getUsedVoucherList(categoryId: String) = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        UsedVoucherPagingSource(api, sessionData, categoryId)
    }.flow
}