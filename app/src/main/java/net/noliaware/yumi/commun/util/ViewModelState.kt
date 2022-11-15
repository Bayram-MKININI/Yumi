package net.noliaware.yumi.commun.util

sealed interface ViewModelState<T> {
    data class DataState<T>(val data: T? = null) : ViewModelState<T>
    class LoadingState<T> : ViewModelState<T>
}
