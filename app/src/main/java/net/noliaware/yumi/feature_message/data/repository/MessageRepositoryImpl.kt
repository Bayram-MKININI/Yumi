package net.noliaware.yumi.feature_message.data.repository

import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData

class MessageRepositoryImpl(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : MessageRepository {

}