package net.noliaware.yumi.commun.util

sealed class Resource<T>(
    val data: T? = null,
    val dataError: DataError = DataError.NONE,
    val errorCode: Int? = null
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(dataError: DataError, errorCode: Int? = null) :
        Resource<T>(dataError = dataError, errorCode = errorCode)
}