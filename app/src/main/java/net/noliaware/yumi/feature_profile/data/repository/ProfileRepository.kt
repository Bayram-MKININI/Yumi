package net.noliaware.yumi.feature_profile.data.repository

import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_alerts.domain.model.Alert
import net.noliaware.yumi.feature_login.domain.model.AccountData
import net.noliaware.yumi.feature_login.domain.model.InitData
import net.noliaware.yumi.feature_categories.domain.model.Voucher

interface ProfileRepository {

    fun getInitData(androidId: String, deviceId: String?, login: String): Flow<Resource<InitData>>

    fun getConnectData(password: String): Flow<Resource<AccountData>>

    fun getVoucherList(categoryId: String): Flow<Resource<List<Voucher>>>

    fun getAlertList(): Flow<Resource<List<Alert>>>
}