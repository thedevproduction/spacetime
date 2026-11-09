package com.spacetime.evcharging.core.common.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole(val displayName: String, val roleKey: String) {
    CUSTOMER("EV Customer", "CUSTOMER"),
    SITE_ADMIN("Station Owner", "SITE_ADMIN"),
    SERVICE_PROVIDER("Service Provider", "SERVICE_PROVIDER"),
    SUPER_ADMIN("Platform Admin", "SUPER_ADMIN");

    companion object {
        fun fromKey(key: String): UserRole = entries.find { it.roleKey.equals(key, ignoreCase = true) } ?: CUSTOMER
    }
}
