package net.noliaware.yumi.commun.data.remote

import net.noliaware.yumi.commun.*
import net.noliaware.yumi.commun.ApiConstants.CONNECT
import net.noliaware.yumi.commun.ApiConstants.DELETE_INBOX_MESSAGE
import net.noliaware.yumi.commun.ApiConstants.DELETE_OUTBOX_MESSAGE
import net.noliaware.yumi.commun.ApiConstants.GET_ACCOUNT
import net.noliaware.yumi.commun.ApiConstants.GET_ALERT_LIST
import net.noliaware.yumi.commun.ApiConstants.GET_AVAILABLE_DATA_PER_CATEGORY
import net.noliaware.yumi.commun.ApiConstants.GET_AVAILABLE_VOUCHER_LIST_BY_CATEGORY
import net.noliaware.yumi.commun.ApiConstants.GET_BACK_OFFICE_SIGN_IN_CODE
import net.noliaware.yumi.commun.ApiConstants.GET_CANCELLED_DATA_PER_CATEGORY
import net.noliaware.yumi.commun.ApiConstants.GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY
import net.noliaware.yumi.commun.ApiConstants.GET_INBOX_MESSAGE
import net.noliaware.yumi.commun.ApiConstants.GET_INBOX_MESSAGE_LIST
import net.noliaware.yumi.commun.ApiConstants.GET_OUTBOX_MESSAGE
import net.noliaware.yumi.commun.ApiConstants.GET_OUTBOX_MESSAGE_LIST
import net.noliaware.yumi.commun.ApiConstants.GET_USED_DATA_PER_CATEGORY
import net.noliaware.yumi.commun.ApiConstants.GET_USED_VOUCHER_LIST_BY_CATEGORY
import net.noliaware.yumi.commun.ApiConstants.GET_VOUCHER
import net.noliaware.yumi.commun.ApiConstants.GET_VOUCHER_REQUEST_LIST
import net.noliaware.yumi.commun.ApiConstants.GET_VOUCHER_STATUS
import net.noliaware.yumi.commun.ApiConstants.INIT
import net.noliaware.yumi.commun.ApiConstants.SEND_MESSAGE
import net.noliaware.yumi.commun.ApiConstants.SEND_VOUCHER_REQUEST
import net.noliaware.yumi.commun.ApiConstants.SET_PRIVACY_POLICY_READ_STATUS
import net.noliaware.yumi.commun.ApiConstants.USE_VOUCHER
import net.noliaware.yumi.commun.ApiParameters.SALT_STRING
import net.noliaware.yumi.commun.ApiParameters.TIMESTAMP
import net.noliaware.yumi.commun.ApiParameters.TOKEN
import net.noliaware.yumi.commun.data.remote.dto.ResponseDTO
import net.noliaware.yumi.feature_alerts.data.remote.dto.AlertsDTO
import net.noliaware.yumi.feature_categories.data.remote.dto.*
import net.noliaware.yumi.feature_login.data.remote.dto.AccountDataDTO
import net.noliaware.yumi.feature_login.data.remote.dto.InitDTO
import net.noliaware.yumi.feature_message.data.remote.dto.*
import net.noliaware.yumi.feature_profile.data.remote.dto.*
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface RemoteApi {

    @FormUrlEncoded
    @POST("$INIT/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchInitData(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<InitDTO>

    @FormUrlEncoded
    @POST("$CONNECT/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchAccountDataForPassword(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<AccountDataDTO>

    @FormUrlEncoded
    @POST("$SET_PRIVACY_POLICY_READ_STATUS/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun updatePrivacyPolicyReadStatus(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<UpdatePrivacyPolicyResponseDTO>

    @FormUrlEncoded
    @POST("$GET_AVAILABLE_DATA_PER_CATEGORY/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchAvailableDataByCategory(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<AvailableVoucherCategoriesDTO>

    @FormUrlEncoded
    @POST("$GET_USED_DATA_PER_CATEGORY/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchUsedDataByCategory(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<UsedVoucherCategoriesDTO>

    @FormUrlEncoded
    @POST("$GET_CANCELLED_DATA_PER_CATEGORY/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchCancelledDataByCategory(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<CancelledVoucherCategoriesDTO>

    @FormUrlEncoded
    @POST("$GET_AVAILABLE_VOUCHER_LIST_BY_CATEGORY/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchVouchersForCategory(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<VouchersDTO>

    @FormUrlEncoded
    @POST("$GET_VOUCHER/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchVoucherForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<GetVoucherDTO>

    @FormUrlEncoded
    @POST("${SEND_VOUCHER_REQUEST}/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun sendVoucherRequestWithId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<SendVoucherRequestDTO>

    @FormUrlEncoded
    @POST("$GET_VOUCHER_REQUEST_LIST/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchVoucherRequestListForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<VoucherRequestsDTO>

    @FormUrlEncoded
    @POST("$GET_VOUCHER_STATUS/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchVoucherStatusForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<GetVoucherStateDataDTO>

    @FormUrlEncoded
    @POST("$USE_VOUCHER/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun useVoucher(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<UseVoucherResponseDTO>

    @FormUrlEncoded
    @POST("$GET_ACCOUNT/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchAccount(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<UserAccountDTO>

    @FormUrlEncoded
    @POST("$GET_BACK_OFFICE_SIGN_IN_CODE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchBackOfficeSignInCode(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<BOSignInDTO>

    @FormUrlEncoded
    @POST("$GET_USED_VOUCHER_LIST_BY_CATEGORY/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchUsedVouchersForCategory(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<UsedVouchersDTO>

    @FormUrlEncoded
    @POST("$GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchCancelledVouchersForCategory(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<CancelledVouchersDTO>

    @FormUrlEncoded
    @POST("$GET_INBOX_MESSAGE_LIST/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchInboxMessages(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<InboxMessagesDTO>

    @FormUrlEncoded
    @POST("$GET_INBOX_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchInboxMessageForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<InboxMessageDTO>

    @FormUrlEncoded
    @POST("$GET_OUTBOX_MESSAGE_LIST/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchOutboxMessages(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<OutboxMessagesDTO>

    @FormUrlEncoded
    @POST("$GET_OUTBOX_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchOutboxMessageForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<OutboxMessageDTO>

    @FormUrlEncoded
    @POST("$SEND_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun sendMessage(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<SentMessageDTO>

    @FormUrlEncoded
    @POST("$DELETE_INBOX_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun deleteInboxMessageForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<DeleteInboxMessageDTO>

    @FormUrlEncoded
    @POST("$DELETE_OUTBOX_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun deleteOutboxMessageForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<DeleteOutboxMessageDTO>

    @FormUrlEncoded
    @POST("$GET_ALERT_LIST/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchAlertList(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<AlertsDTO>
}