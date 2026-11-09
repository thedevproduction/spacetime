package com.spacetime.evcharging.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.spacetime.evcharging.core.designsystem.theme.*
import com.spacetime.evcharging.core.navigation.*
import com.spacetime.evcharging.feature.customer.home.CustomerHomeScreen
import com.spacetime.evcharging.feature.provider.dashboard.ProviderDashboardScreen
import com.spacetime.evcharging.feature.site.dashboard.SiteAdminDashboardScreen
import com.spacetime.evcharging.feature.station.discovery.ExploreMapScreen

@Composable
fun CustomerShell(
    rootNavController: NavController
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = CardBackground) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(if (selectedTab == 0) Icons.Default.Home else Icons.Outlined.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(if (selectedTab == 1) Icons.Default.Map else Icons.Outlined.Map, contentDescription = "Explore") },
                    label = { Text("Explore") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(if (selectedTab == 2) Icons.Default.ElectricBolt else Icons.Outlined.ElectricBolt, contentDescription = "My Charging") },
                    label = { Text("My Charging") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(if (selectedTab == 3) Icons.Default.AccountBalanceWallet else Icons.Outlined.AccountBalanceWallet, contentDescription = "Wallet") },
                    label = { Text("Wallet") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(if (selectedTab == 4) Icons.Default.Person else Icons.Outlined.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (selectedTab) {
                0 -> CustomerHomeScreen(
                    onNavigateToQr = { rootNavController.navigate(QrScannerRoute) },
                    onNavigateToWallet = { selectedTab = 3 },
                    onNavigateToExplore = { selectedTab = 1 },
                    onNavigateToStation = { stationId ->
                        rootNavController.navigate(StationDetailsRoute(stationId))
                    },
                    onNavigateToProfile = { selectedTab = 4 }
                )
                1 -> ExploreMapScreen(
                    onNavigateToStationDetails = { stationId ->
                        rootNavController.navigate(StationDetailsRoute(stationId))
                    }
                )
                2 -> com.spacetime.evcharging.core.designsystem.component.EmptyState(
                    title = "My Charging Sessions",
                    message = "Your active and historical EV charging sessions will appear here."
                )
                3 -> com.spacetime.evcharging.feature.wallet.WalletDashboardScreen(
                    onNavigateToAddMoney = { rootNavController.navigate(AddMoneyRoute) },
                    onNavigateToTransactions = { rootNavController.navigate(WalletTransactionsRoute) }
                )
                4 -> com.spacetime.evcharging.feature.customer.profile.CustomerProfileScreen(
                    onNavigateToVehicles = { rootNavController.navigate(VehiclesRoute) },
                    onNavigateToWallet = { selectedTab = 3 },
                    onNavigateToSavedStations = { rootNavController.navigate(SavedStationsRoute) },
                    onNavigateToNotifications = { rootNavController.navigate(NotificationsRoute) },
                    onNavigateToHelp = { rootNavController.navigate(HelpSupportRoute) },
                    onNavigateToRoleSelection = { rootNavController.navigate(RoleSelectionRoute) },
                    onLogout = { rootNavController.navigate(SplashRoute) { popUpTo(0) { inclusive = true } } }
                )
            }
        }
    }
}

@Composable
fun SiteAdminShell(
    rootNavController: NavController
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = CardBackground) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.EvStation, contentDescription = "Stations") },
                    label = { Text("Stations") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.History, contentDescription = "Sessions") },
                    label = { Text("Sessions") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Payments, contentDescription = "Revenue") },
                    label = { Text("Revenue") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.People, contentDescription = "Personnel") },
                    label = { Text("Staff") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (selectedTab) {
                0 -> SiteAdminDashboardScreen(
                    onNavigateToProfile = { rootNavController.navigate(RoleSelectionRoute) }
                )
                1 -> com.spacetime.evcharging.feature.site.stations.SiteStationsScreen()
                2 -> com.spacetime.evcharging.feature.site.sessions.SiteSessionsScreen()
                3 -> com.spacetime.evcharging.feature.site.revenue.SiteRevenueScreen()
                4 -> com.spacetime.evcharging.feature.site.employees.SiteEmployeesScreen()
            }
        }
    }
}

@Composable
fun ProviderShell(
    rootNavController: NavController
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = CardBackground) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Construction, contentDescription = "Installations") },
                    label = { Text("Installations") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.SupportAgent, contentDescription = "Service") },
                    label = { Text("Service") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Inventory2, contentDescription = "Equipment") },
                    label = { Text("Equipment") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.People, contentDescription = "Personnel") },
                    label = { Text("Staff") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = Primary.copy(alpha = 0.12f))
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (selectedTab) {
                0 -> ProviderDashboardScreen(
                    onNavigateToProfile = { rootNavController.navigate(RoleSelectionRoute) }
                )
                1 -> com.spacetime.evcharging.feature.provider.installations.ProviderInstallationsScreen()
                2 -> com.spacetime.evcharging.feature.provider.service.ProviderServiceScreen()
                3 -> com.spacetime.evcharging.feature.provider.equipment.ProviderEquipmentScreen()
                4 -> com.spacetime.evcharging.feature.provider.technicians.ProviderTechniciansScreen()
            }
        }
    }
}
