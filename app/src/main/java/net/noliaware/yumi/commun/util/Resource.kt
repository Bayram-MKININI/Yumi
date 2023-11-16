package net.noliaware.yumi.commun.util

import net.noliaware.yumi.commun.domain.model.AppMessage
import net.noliaware.yumi.commun.util.ServiceError.ErrNone

sealed interface Resource<T> {
    class Loading<T> : Resource<T>

    data class Success<T>(
        val data: T,
        val appMessage: AppMessage? = null
    ) : Resource<T>

    data class Error<T>(
        val appMessage: AppMessage? = null,
        val serviceError: ServiceError = ErrNone
    ) : Resource<T>
}