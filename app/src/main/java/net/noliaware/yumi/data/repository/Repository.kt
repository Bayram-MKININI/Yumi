package net.noliaware.yumi.data.repository

import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.domain.model.ConnectData
import net.noliaware.yumi.domain.model.InitData
import net.noliaware.yumi.domain.model.Voucher

interface Repository {

    fun getInitData(androidId: String, deviceId: String?, login: String): Flow<Resource<InitData>>

    fun getConnectData(password: String): Flow<Resource<ConnectData>>

    fun getVoucherList(categoryId: String): Flow<Resource<List<Voucher>>>
}