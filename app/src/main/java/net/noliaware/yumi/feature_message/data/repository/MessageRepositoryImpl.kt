package net.noliaware.yumi.feature_message.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.feature_message.domain.model.Message

class MessageRepositoryImpl(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : MessageRepository {

    override fun getInboxMessageList(): Flow<Resource<List<Message>>> = flow {

    }

    override fun getInboxMessage(): Flow<Resource<Message>> = flow {

    }

    override fun getOutboxMessageList(): Flow<Resource<List<Message>>> = flow {

    }

    override fun getOutboxMessage(): Flow<Resource<Message>> = flow {

    }

    override fun sendMessage(): Flow<Resource<Boolean>> = flow {

    }
}