package net.noliaware.yumi.feature_login.data.repository

import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_login.domain.model.InitData

interface LoginRepository {

    fun getInitData(androidId: String, deviceId: String?, login: String): Flow<Resource<InitData>>

    fun getAccountData(password: String): Flow<Resource<AccountData>>
}