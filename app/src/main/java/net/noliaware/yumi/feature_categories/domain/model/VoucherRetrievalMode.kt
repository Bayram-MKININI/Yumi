package net.noliaware.yumi.feature_categories.domain.model

enum class VoucherRetrievalMode(val value: Int) {
    BENEFICIARY(1),
    CONTRIBUTOR(2),
    BOTH(3);

    companion object {
        fun fromValue(value: Int?) = entries.firstOrNull { it.value == value }
    }
}