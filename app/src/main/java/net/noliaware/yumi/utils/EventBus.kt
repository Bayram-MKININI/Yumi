package net.noliaware.yumi.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EventBus {
    private val _events = MutableSharedFlow<Any>()
    val events = _events.asSharedFlow()

    suspend fun produceEvent(event: Any) {
        _events.emit(event) // suspends until all subscribers receive it
    }
}