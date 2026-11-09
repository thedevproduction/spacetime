package com.spacetime.evcharging.core.common.model

import kotlinx.serialization.Serializable

@Serializable
enum class PaymentStatus(val displayName: String) {
    CREATED("Created"),
    PENDING("Pending"),
    AUTHORIZED("Authorized"),
    CAPTURED("Captured"),
    FAILED("Failed"),
    CANCELLED("Cancelled"),
    REFUND_PENDING("Refund Pending"),
    PARTIALLY_REFUNDED("Partially Refunded"),
    REFUNDED("Refunded"),
    RECONCILIATION_PENDING("Reconciliation Pending")
}
