package net.noliaware.yumi.feature_login.domain.model

data class InitData(
    val deviceId: String = "",
    val keyboard: List<Int> = listOf()
)