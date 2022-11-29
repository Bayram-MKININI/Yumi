package net.noliaware.yumi.feature_login.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_login.domain.model.InitData
import net.noliaware.yumi.feature_profile.domain.model.UserProfile

interface LoginRepository {

    fun getInitData(androidId: String, deviceId: String?, login: String): Flow<Resource<InitData>>

    fun getManagedProfileList(): Flow<PagingData<UserProfile>>

    fun getAccountData(password: String): Flow<Resource<AccountData>>

    fun selectAccountForId(accountId: String): Flow<Resource<AccountData>>
}