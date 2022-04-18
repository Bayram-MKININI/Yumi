package net.noliaware.yumi.feature_message.data.repository

import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_message.domain.model.Message

interface MessageRepository {

    fun getInboxMessageList(): Flow<Resource<List<Message>>>

    fun getInboxMessage(): Flow<Resource<Message>>

    fun getOutboxMessageList(): Flow<Resource<List<Message>>>

    fun getOutboxMessage(): Flow<Resource<Message>>

    fun sendMessage(): Flow<Resource<Boolean>>
}