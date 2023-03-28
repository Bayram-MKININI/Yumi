package net.noliaware.yumi.feature_categories.domain.model

enum class VoucherStatus(val value: Int) {
    USABLE(1),
    CONSUMED(2),
    CANCELLED(-1),
    INEXISTENT(3);
    companion object {
        fun fromValue(value: Int?) = VoucherStatus.values().firstOrNull { it.value == value }
    }
}