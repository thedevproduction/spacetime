package com.spacetime.evcharging.core.navigation

import androidx.compose.ui.graphics.vector.ImageVector

interface BottomNavItem {
    val route: Any
    val label: String
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
}
