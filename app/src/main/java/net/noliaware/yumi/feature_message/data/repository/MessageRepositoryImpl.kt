package net.noliaware.yumi.feature_message.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi.commun.*
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.*
import net.noliaware.yumi.feature_message.domain.model.Message
import okio.IOException
import retrofit2.HttpException
import java.util.*

class MessageRepositoryImpl(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : MessageRepository {

    override fun getMessageList(): Flow<Resource<List<Message>>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchInboxMessages(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    GET_INBOX_MESSAGE_LIST,
                    randomString
                ),
                params = generateGetMessagesListParams()
            )

            val sessionNoFailure =
                !handleSessionFailure(remoteData.session, sessionData, remoteData.error)

            if (sessionNoFailure) {
                remoteData.data?.let { inboxMessagesDTO ->

                    val mutableList: MutableList<Message> = mutableListOf()

                    mutableList.addAll(inboxMessagesDTO.messageList.map { it.toMessage() })

                    getOutboxMessages().data?.let { outboxMessageList ->
                        mutableList.addAll(outboxMessageList)
                    }

                    emit(Resource.Success(data = mutableList))
                }
            }

        } catch (ex: HttpException) {

            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))

        } catch (ex: IOException) {

            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
        }
    }

    private fun generateGetMessagesListParams() = mutableMapOf(
        LIMIT to "0",
        OFFSET to "0"
    ).also { it.plusAssign(getCommonWSParams(sessionData)) }

    private suspend fun getOutboxMessages(): Resource<List<Message>> {

        val timestamp = System.currentTimeMillis().toString()
        val randomString = UUID.randomUUID().toString()

        val remoteCategoriesData = api.fetchOutboxMessages(
            timestamp = timestamp,
            saltString = randomString,
            token = generateToken(
                timestamp,
                GET_OUTBOX_MESSAGE_LIST,
                randomString
            ),
            params = generateGetMessagesListParams()
        )

        remoteCategoriesData.data?.let { messagesDTO ->

            remoteCategoriesData.session?.let { sessionDTO ->
                sessionData.apply {
                    sessionId = sessionDTO.sessionId
                    sessionToken = sessionDTO.sessionToken
                }
            }

            return Resource.Success(data = messagesDTO.messageList.map { it.toMessage() })
        }

        return Resource.Error(errorType = ErrorType.SYSTEM_ERROR)
    }

    override fun getInboxMessageForId(messageId: String): Flow<Resource<Message>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            Log.e("params", generateGetMessageParams(messageId).toString())

            val remoteData = api.fetchInboxMessageForId(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    GET_INBOX_MESSAGE,
                    randomString
                ),
                params = generateGetMessageParams(messageId)
            )

            val sessionNoFailure =
                !handleSessionFailure(remoteData.session, sessionData, remoteData.error)

            if (sessionNoFailure) {
                remoteData.data?.let { singleMessageDTO ->
                    emit(Resource.Success(data = singleMessageDTO.message.toMessage()))
                }
            }

        } catch (ex: HttpException) {

            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))

        } catch (ex: IOException) {

            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
        }
    }

    private fun generateGetMessageParams(messageId: String) = mutableMapOf(
        MESSAGE_ID to messageId
    ).also { it.plusAssign(getCommonWSParams(sessionData)) }

    override fun getOutboxMessageForId(messageId: String): Flow<Resource<Message>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchOutboxMessageForId(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    GET_OUTBOX_MESSAGE,
                    randomString
                ),
                params = generateGetMessageParams(messageId)
            )

            val sessionNoFailure =
                !handleSessionFailure(remoteData.session, sessionData, remoteData.error)

            if (sessionNoFailure) {
                remoteData.data?.let { singleMessageDTO ->
                    emit(Resource.Success(data = singleMessageDTO.message.toMessage()))
                }
            }

        } catch (ex: HttpException) {

            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))

        } catch (ex: IOException) {

            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
        }
    }

    override fun sendMessage(messageSubject: String, messageBody: String): Flow<Resource<Boolean>> =
        flow {

            emit(Resource.Loading())

            try {

                val timestamp = System.currentTimeMillis().toString()
                val randomString = UUID.randomUUID().toString()

                val remoteData = api.sendMessage(
                    timestamp = timestamp,
                    saltString = randomString,
                    token = generateToken(
                        timestamp,
                        SEND_MESSAGE,
                        randomString
                    ),
                    params = generateSendMessageParams(messageSubject, messageBody)
                )

                val sessionNoFailure =
                    !handleSessionFailure(remoteData.session, sessionData, remoteData.error)

                if (sessionNoFailure) {
                    emit(Resource.Success(data = true))
                }

            } catch (ex: HttpException) {

                emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))

            } catch (ex: IOException) {

                emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
            }
        }

    private fun generateSendMessageParams(messageSubject: String, messageBody: String) =
        mutableMapOf(
            MESSAGE_SUBJECT to messageSubject,
            MESSAGE_BODY to messageBody
        ).also { it.plusAssign(getCommonWSParams(sessionData)) }
}