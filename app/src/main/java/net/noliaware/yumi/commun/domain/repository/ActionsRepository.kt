package net.noliaware.yumi.commun.domain.repository

import net.noliaware.yumi.commun.domain.model.Action

interface ActionsRepository {
    suspend fun performActions(actions: List<Action>)
}