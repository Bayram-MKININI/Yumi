package net.noliaware.yumi.data.remote

import net.noliaware.yumi.data.remote.dto.ConnectDTO
import net.noliaware.yumi.data.remote.dto.InitDTO
import net.noliaware.yumi.data.remote.dto.ResponseDTO
import net.noliaware.yumi.data.remote.dto.VoucherDTO
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface RemoteApi {

    @FormUrlEncoded
    @POST("/yumi/user/init/{timestamp}/{saltString}/{token}")
    suspend fun fetchInitData(
        @Path("timestamp") timestamp: String,
        @Path("saltString") saltString: String,
        @Path("token") token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<InitDTO>

    @FormUrlEncoded
    @POST("/yumi/user/connect/{timestamp}/{saltString}/{token}")
    suspend fun fetchConnectData(
        @Path("timestamp") timestamp: String,
        @Path("saltString") saltString: String,
        @Path("token") token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<ConnectDTO>

    @FormUrlEncoded
    @POST("/yumi/user/getAvailableVoucherListByCategory/{timestamp}/{saltString}/{token}")
    suspend fun fetchVouchersByCategory(
        @Path("timestamp") timestamp: String,
        @Path("saltString") saltString: String,
        @Path("token") token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<List<VoucherDTO>>
}