package com.spacetime.evcharging.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoneyDto(
    @SerialName("amount_minor") val amountMinor: Long,
    @SerialName("currency") val currency: String = "INR",
    @SerialName("display_amount") val displayAmount: String? = null,
    @SerialName("unit") val unit: String? = null
)
