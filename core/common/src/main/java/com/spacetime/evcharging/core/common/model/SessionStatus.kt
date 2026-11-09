package com.spacetime.evcharging.core.common.model

import kotlinx.serialization.Serializable

@Serializable
enum class SessionStatus(val displayName: String) {
    CREATED("Created"),
    PAYMENT_PENDING("Payment Pending"),
    PAYMENT_AUTHORIZED("Payment Authorized"),
    STARTING("Starting"),
    CHARGING("Charging"),
    STOPPING("Stopping"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    INTERRUPTED("Interrupted"),
    CANCELLED("Cancelled"),
    RECONCILIATION_PENDING("Reconciliation Pending");

    val isActive: Boolean
        get() = this in listOf(STARTING, CHARGING, STOPPING)
}
