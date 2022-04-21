package net.noliaware.yumi.commun.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import net.noliaware.yumi.commun.util.Resource
import net.noliaware.yumi.commun.util.UIEvent
import net.noliaware.yumi.commun.util.ViewModelState

abstract class BaseViewModel<T> : ViewModel() {

    private val _stateFlow = MutableStateFlow(ViewModelState<T>())
    val stateFlow = _stateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    internal suspend fun handleResponse(result: Resource<T>) {
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