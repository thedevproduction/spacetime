package com.spacetime.evcharging.core.common

sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Error(
        val code: String? = null,
        val message: String,
        val retryable: Boolean = false
    ) : AppResult<Nothing>
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(data))
    is AppResult.Error -> this
}

inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) action(data)
    return this
}

inline fun <T> AppResult<T>.onError(action: (code: String?, message: String) -> Unit): AppResult<T> {
    if (this is AppResult.Error) action(code, message)
    return this
}
