package net.noliaware.yumi.model

private const val serverName = "https://api.noliaware.net/api/ketzal/"
const val INIT_DATA_URL = serverName + "init_v1"
const val CONNECT_URL = serverName + "connect_v1"

const val GET_MESSAGES_INBOX_URL = serverName + "get_inbox_messages_v1"
const val GET_MESSAGES_OUTBOX_URL = serverName + "get_outbox_messages_v1"
const val SEND_MESSAGES_URL = serverName + "send_message_v1"

const val GET_ALERTS_URL = serverName + "get_alerts_v1"
const val GET_INFO_URL = serverName + "get_infos_v1"

const val LOGIN = "login"
const val SESSION_ID = "session_id"
const val TOKEN = "token"
const val KEYBOARD = "keyboard"
const val ENCRYPTION_VECTOR = "encryption_vector"
const val LIMIT = "limit"
const val OFFSET = "offset"
const val SUBJECT = "subject"
const val MESSAGE = "message"
const val MESSAGES = "messages"
const val ALERTS = "alerts"

const val MESSAGE_INBOX_DAT = "message_inbox_dat"
const val MESSAGE_OUTBOX_DAT = "message_outbox_dat"

//FRAGMENT TAGS
const val ACCOUNTS_LIST_FRAGMENT_TAG = "accounts_list_fragment"
const val VOUCHERS_LIST_FRAGMENT_TAG = "vouchers_list_fragment"
const val VOUCHERS_DETAILS_FRAGMENT_TAG = "vouchers_details_fragment"
const val READ_MESSAGE_FRAGMENT_TAG = "read_message_fragment"
const val SEND_MESSAGES_FRAGMENT_TAG = "send_messages_fragment"

const val GOLDEN_RATIO = 1.6180339887