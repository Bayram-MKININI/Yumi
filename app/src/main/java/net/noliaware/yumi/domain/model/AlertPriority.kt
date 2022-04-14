package net.noliaware.yumi.domain.model

enum class AlertPriority {
    RED, ORANGE, NONE;

    companion object {
        fun getAlertPriorityByName(name: String): AlertPriority {
            when (name) {
                "red" -> return RED
                "orange" -> return ORANGE
            }
            return NONE
        }
    }
}