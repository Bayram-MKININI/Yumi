package net.noliaware.yumi.feature_login.domain.model

import java.io.Serializable

data class AccountData(
    val helloMessage: String = "",
    val userName: String = "",
    val availableVoucherCount: Int = 0,
    val messageSubjects: List<MessageSubject>,
    val newAlertCount: Int = 0,
    val newMessageCount: Int = 0
) : Serializable