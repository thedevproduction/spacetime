package com.spacetime.evcharging.core.common

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String, val retryable: Boolean = true) : UiState<Nothing>
    data object Empty : UiState<Nothing>
}
