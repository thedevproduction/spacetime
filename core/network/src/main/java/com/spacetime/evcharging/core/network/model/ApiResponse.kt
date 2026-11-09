package com.spacetime.evcharging.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: T? = null,
    @SerialName("error") val error: ApiError? = null,
    @SerialName("meta") val meta: ApiMeta? = null,
    @SerialName("pagination") val pagination: Pagination? = null
)

@Serializable
data class ApiError(
    @SerialName("code") val code: String,
    @SerialName("message") val message: String,
    @SerialName("details") val details: Map<String, String>? = null,
    @SerialName("retryable") val retryable: Boolean = false
)

@Serializable
data class ApiMeta(
    @SerialName("request_id") val requestId: String? = null
)

@Serializable
data class Pagination(
    @SerialName("page") val page: Int,
    @SerialName("page_size") val pageSize: Int,
    @SerialName("total_items") val totalItems: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("has_next") val hasNext: Boolean
)
