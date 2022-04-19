package net.noliaware.yumi.feature_message.data.repository

import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_message.domain.model.Message

interface MessageRepository {

    fun getMessageList(): Flow<Resource<List<Message>>>

    fun getInboxMessageForId(messageId: String): Flow<Resource<Message>>

    fun getOutboxMessageForId(messageId: String): Flow<Resource<Message>>

    fun sendMessage(messageSubject: String, messageBody: String): Flow<Resource<Boolean>>
}