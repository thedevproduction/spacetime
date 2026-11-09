package com.spacetime.evcharging.core.common.model

import kotlinx.serialization.Serializable

@Serializable
enum class ChargerStatus(val displayName: String) {
    AVAILABLE("Available"),
    PREPARING("Preparing"),
    CHARGING("Charging"),
    SUSPENDED_EV("Suspended (EV)"),
    SUSPENDED_EVSE("Suspended (EVSE)"),
    FINISHING("Finishing"),
    RESERVED("Reserved"),
    UNAVAILABLE("Unavailable"),
    FAULTED("Faulted"),
    OFFLINE("Offline"),
    MAINTENANCE("Maintenance"),
    UNKNOWN("Unknown");

    val isAvailableToCharge: Boolean
        get() = this == AVAILABLE
}
