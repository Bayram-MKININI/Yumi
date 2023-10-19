package net.noliaware.yumi.commun.domain.model

data class Action(
    val type: String = "",
    val params: List<ActionParam>
)