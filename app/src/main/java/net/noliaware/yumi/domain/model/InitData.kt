package net.noliaware.yumi.domain.model

data class InitData(
    val deviceId: String = "",
    val keyboard: List<Int> = listOf()
)