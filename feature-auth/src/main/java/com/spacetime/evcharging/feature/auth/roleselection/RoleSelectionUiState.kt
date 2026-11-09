package com.spacetime.evcharging.feature.auth.roleselection

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.ui.graphics.vector.ImageVector
import com.spacetime.evcharging.core.common.model.UserRole

/**
 * Data class representing the UI state for the Role Selection screen.
 */
data class RoleSelectionUiState(
    val userName: String = "",
    val availableRoles: List<RoleOption> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Represents a selectable role option displayed on the Role Selection screen.
 */
data class RoleOption(
    val role: UserRole,
    val displayName: String,
    val description: String,
    val icon: ImageVector
)

/**
 * Factory function to create the default list of role options from [UserRole] values.
 */
fun createRoleOptions(roles: List<UserRole>): List<RoleOption> {
    return roles.map { role ->
        when (role) {
            UserRole.CUSTOMER -> RoleOption(
                role = UserRole.CUSTOMER,
                displayName = "Customer",
                description = "Find charging stations and charge your EV",
                icon = Icons.Default.ElectricCar
            )
            UserRole.SITE_ADMIN -> RoleOption(
                role = UserRole.SITE_ADMIN,
                displayName = "Site Admin",
                description = "Manage your charging stations",
                icon = Icons.Default.Build
            )
            UserRole.SERVICE_PROVIDER -> RoleOption(
                role = UserRole.SERVICE_PROVIDER,
                displayName = "Service Provider",
                description = "Manage installations and service",
                icon = Icons.Default.Engineering
            )
            UserRole.SUPER_ADMIN -> RoleOption(
                role = UserRole.SUPER_ADMIN,
                displayName = "Super Admin",
                description = "System administrator",
                icon = Icons.Default.Build
            )
        }
    }
}
