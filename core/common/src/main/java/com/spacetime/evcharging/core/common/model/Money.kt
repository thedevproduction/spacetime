package com.spacetime.evcharging.core.common.model

import com.spacetime.evcharging.core.common.Constants
import com.spacetime.evcharging.core.common.toFormattedCurrency
import kotlinx.serialization.Serializable

@Serializable
data class Money(
    val amountMinor: Long,
    val currency: String = Constants.CURRENCY_INR,
    val displayAmount: String = amountMinor.toFormattedCurrency(currency)
)
