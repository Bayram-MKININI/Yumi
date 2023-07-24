package net.noliaware.yumi.commun

import net.noliaware.yumi.BuildConfig


const val BASE_URL = "https://api.noliaware.net/yumi/user/"
const val INIT = "init"
const val CONNECT = "connect"
const val SET_PRIVACY_POLICY_READ_STATUS = "setPrivacyPolicyReadStatus"
const val GET_AVAILABLE_VOUCHER_LIST_BY_CATEGORY = "getAvailableVoucherListByCategory"
const val GET_VOUCHER = "getVoucher"
const val GET_VOUCHER_STATUS = "getVoucherStatus"
const val USE_VOUCHER = "useVoucher"
const val GET_ACCOUNT = "getAccount"
const val GET_BACK_OFFICE_SIGN_IN_CODE = "getBackOfficeSignInCode"
const val GET_AVAILABLE_DATA_PER_CATEGORY = "getAvailableVoucherDataPerCategory"
const val GET_USED_DATA_PER_CATEGORY = "getUsedVoucherDataPerCategory"
const val GET_CANCELLED_DATA_PER_CATEGORY = "getCanceledVoucherDataPerCategory"
const val GET_USED_VOUCHER_LIST_BY_CATEGORY = "getUsedVoucherListByCategory"
const val GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY = "getCanceledVoucherListByCategory"
const val GET_ALERT_LIST = "getAlertList"
const val GET_INBOX_MESSAGE_LIST = "getInboxMessageList"
const val GET_INBOX_MESSAGE = "getInboxMessage"
const val GET_OUTBOX_MESSAGE_LIST = "getOutboxMessageList"
const val GET_OUTBOX_MESSAGE = "getOutboxMessage"
const val SEND_MESSAGE = "sendMessage"
const val DELETE_INBOX_MESSAGE = "delInboxMessage"
const val DELETE_OUTBOX_MESSAGE = "delOutboxMessage"

const val LOGIN = "login"
const val PASSWORD = "password"
const val APP_VERSION = "appVersion"
const val DEVICE_ID = "deviceId"
const val PUSH_TOKEN = "devicePushToken"
const val DEVICE_TYPE = "deviceType"
const val DEVICE_OS = "deviceOS"
const val DEVICE_UUID = "deviceUUID"
const val DEVICE_LABEL = "deviceLabel"
const val SESSION_ID = "sessionId"
const val SESSION_TOKEN = "sessionToken"
const val TIMESTAMP = "timestamp"
const val SALT_STRING = "saltString"
const val TOKEN = "token"
const val KEYBOARD = "keyboard"
const val ENCRYPTION_VECTOR = "encryption_vector"
const val VOUCHER_ID = "voucherId"
const val VOUCHER_CODE_DATA = "voucherCodeData"
const val LIMIT = "limit"
const val LIST_PAGE_SIZE = 20
const val OFFSET = "offset"
const val MESSAGE_ID = "messageId"
const val MESSAGE_PRIORITY = "messagePriority"
const val MESSAGE_SUBJECT_ID = "messageSubjectId"
const val MESSAGE_SUBJECT_LABEL = "messageSubjectLabel"
const val MESSAGE_BODY = "messageBody"
const val MESSAGE = "message"
const val MESSAGES = "messages"
const val ALERTS = "alerts"
const val TIMESTAMP_OFFSET = "timestampOffset"

const val ACCOUNT_DATA = "account_data"
const val PRIVACY_POLICY_URL = "privacy_policy_url"
const val PRIVACY_POLICY_CONFIRMATION_REQUIRED = "privacy_policy_confirmation_required"
const val MESSAGE_SUBJECTS_DATA = "message_subjects_data"
const val CATEGORY_ID = "categoryId"
const val CATEGORY_LABEL = "categoryLabel"
const val CATEGORY = "category"
const val CATEGORY_UI = "categoryUI"

const val MESSAGE_INBOX_DAT = "message_inbox_dat"
const val MESSAGE_OUTBOX_DAT = "message_outbox_dat"

const val DATA_SHOULD_REFRESH = "dataShouldRefresh"
const val SELECTED_MESSAGE_INDEX = "selectedMessageIndex"

const val ACTION_PUSH_DATA = BuildConfig.APPLICATION_ID + ".action.PUSH"
const val PUSH_TITLE = "title"
const val PUSH_BODY = "body"

const val DATE_TIME_SOURCE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS"
const val DATE_SOURCE_FORMAT = "yyyy-MM-dd"
const val TIME_SOURCE_FORMAT = "HH:mm:ss"

const val NUMERICAL_DATE_FORMAT = "dd/MM/yyyy"
const val SINGLE_DAY_DATE_FORMAT = "EEE"
const val DAY_OF_MONTH_NUMERICAL_DATE_FORMAT = "dd/MM"
const val LONG_DATE_WITH_DAY_FORMAT = "EEEE dd LLLL yyyy"
const val SHORT_DATE_FORMAT = "dd LLL yyyy"
const val HOURS_TIME_FORMAT = "HH:mm"
const val MINUTES_TIME_FORMAT = "mm:ss"

const val KEY_CURRENT_VERSION = "android_force_update_current_version"
const val KEY_FORCE_UPDATE_REQUIRED = "android_force_update_required"
const val KEY_FORCE_UPDATE_URL = "android_force_update_store_url"

//FRAGMENT TAGS
const val PRIVACY_POLICY_FRAGMENT_TAG = "privacy_policy_fragment"
const val AVAILABLE_VOUCHERS_LIST_FRAGMENT_TAG = "available_vouchers_list_fragment"
const val BO_SIGN_IN_FRAGMENT_TAG = "bo_sign_in_fragment"
const val USED_VOUCHERS_LIST_FRAGMENT_TAG = "used_vouchers_list_fragment"
const val CANCELLED_VOUCHERS_LIST_FRAGMENT_TAG = "cancelled_vouchers_list_fragment"
const val VOUCHER_DETAILS_FRAGMENT_TAG = "voucher_details_fragment"
const val QR_CODE_FRAGMENT_TAG = "qr_code_fragment"
const val READ_MESSAGE_FRAGMENT_TAG = "read_message_fragment"
const val SEND_MESSAGES_FRAGMENT_TAG = "send_messages_fragment"

const val GOLDEN_RATIO = 1.6180339887
const val ONE_HOUR = 3600L