package net.noliaware.yumi.commun.util

sealed interface ViewState<T> {
    data class DataState<T>(val data: T? = null) : ViewState<T>
    class LoadingState<T> : ViewState<T>
}
