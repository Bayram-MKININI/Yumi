package net.noliaware.yumi.domain.model

import java.io.Serializable

data class ManagedAccount(
    val login: String,
    val title: String,
    val firstName: String,
    val lastName: String,
    val cellNumber: String? = null,
    val availableVoucherCount: Int
) : Serializable