package net.noliaware.yumi.commun.util

sealed class Resource<T>(
    val data: T? = null,
    val errorType: ErrorType = ErrorType.NONE,
    val errorMessage: String? = null
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(errorType: ErrorType, errorMessage: String? = null) : Resource<T>(errorType = errorType, errorMessage = errorMessage)
}