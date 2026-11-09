package com.spacetime.evcharging.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WalletDto(
    @SerialName("wallet_id") val walletId: String,
    @SerialName("currency") val currency: String = "INR",
    @SerialName("available_balance_minor") val availableBalanceMinor: Long,
    @SerialName("reserved_balance_minor") val reservedBalanceMinor: Long = 0,
    @SerialName("promotional_balance_minor") val promotionalBalanceMinor: Long = 0,
    @SerialName("wallet_status") val walletStatus: String = "ACTIVE"
)

@Serializable
data class TopUpRequest(
    @SerialName("amount_minor") val amountMinor: Long,
    @SerialName("currency") val currency: String = "INR",
    @SerialName("payment_method") val paymentMethod: String = "UPI"
)

@Serializable
data class TopUpResponse(
    @SerialName("topup_id") val topupId: String,
    @SerialName("payment_order_id") val paymentOrderId: String,
    @SerialName("amount_minor") val amountMinor: Long,
    @SerialName("status") val status: String
)

@Serializable
data class WalletTransactionDto(
    @SerialName("transaction_id") val transactionId: String,
    @SerialName("type") val type: String,
    @SerialName("amount_minor") val amountMinor: Long,
    @SerialName("currency") val currency: String = "INR",
    @SerialName("status") val status: String,
    @SerialName("related_session_id") val relatedSessionId: String? = null,
    @SerialName("reference") val reference: String? = null,
    @SerialName("timestamp") val timestamp: String
)
