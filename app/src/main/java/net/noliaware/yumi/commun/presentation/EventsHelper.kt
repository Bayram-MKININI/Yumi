package net.noliaware.yumi.commun.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.commun.util.UIEvent
import net.noliaware.yumi.commun.util.ViewModelState

class EventsHelper<S> {

    private val _stateFlow = MutableStateFlow(ViewModelState<S>())
    val stateFlow = _stateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    suspend fun handleResponse(result: Resource<S>) {
        when (result) {
            is Resource.Success -> {
                result.data?.let {
                    _stateFlow.value = ViewModelState(it)
                }
            }
            is Resource.Loading -> {
                _stateFlow.value = ViewModelState()
            }
            is Resource.Error -> {
                _stateFlow.value = ViewModelState()
                _eventFlow.emit(UIEvent.ShowSnackBar(result.errorType, result.errorMessage))
            }
        }
    }
}