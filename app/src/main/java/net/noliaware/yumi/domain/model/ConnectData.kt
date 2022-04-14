package net.noliaware.yumi.domain.model

import java.io.Serializable

data class ConnectData(
    val messageSubjects: List<MessageSubject>,
    val newAlertCount: Int = 0,
    val newMessageCount: Int = 0,
    val categories: List<Category>,
    val managedAccounts: List<ManagedAccount>
) : Serializable