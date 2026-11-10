package com.spacetime.evcharging.feature.auth.roleselection

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.spacetime.evcharging.core.common.model.UserRole
import com.spacetime.evcharging.core.designsystem.theme.Primary

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
    val badge: String,
    val accentColor: Color,
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
                description = "Discover nearby hubs, scan QR codes & charge your EV",
                badge = "EV DRIVER",
                accentColor = Primary,
                icon = Icons.Default.ElectricCar
            )
            UserRole.SITE_ADMIN -> RoleOption(
                role = UserRole.SITE_ADMIN,
                displayName = "Site Admin",
                description = "Monitor chargers, sessions, staff & site revenue",
                badge = "STATION HOST",
                accentColor = Color(0xFF1D4ED8),
                icon = Icons.Default.Build
            )
            UserRole.SERVICE_PROVIDER -> RoleOption(
                role = UserRole.SERVICE_PROVIDER,
                displayName = "Service Provider",
                description = "Manage installations, maintenance & field technicians",
                badge = "FIELD OPS",
                accentColor = Color(0xFF0F766E),
                icon = Icons.Default.Engineering
            )
            UserRole.SUPER_ADMIN -> RoleOption(
                role = UserRole.SUPER_ADMIN,
                displayName = "Super Admin",
                description = "System-wide operations & network oversight",
                badge = "ADMIN",
                accentColor = Color(0xFF7C3AED),
                icon = Icons.Default.Build
            )
        }
    }
}
