package com.spacetime.evcharging.feature.customer.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun CustomerProfileScreen(
    onNavigateToVehicles: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToSavedStations: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToRoleSelection: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { SpacetimeTopBar(title = "Account & Settings") },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading && uiState.user == null) {
            LoadingScreen(message = "Loading your account preferences...")
            return@Scaffold
        }

        val user = uiState.user

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Header Card
            Card(
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Primary.copy(alpha = 0.15f),
                            modifier = Modifier.size(60.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = user?.fullName?.take(2)?.uppercase() ?: "EV",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Primary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(user?.fullName ?: "Driver", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MainText)
                            Text(user?.mobileNumber ?: "+91 9876543210", fontSize = 13.sp, color = SecondaryText)
                            Text(user?.email ?: "rahul.sharma@example.com", fontSize = 12.sp, color = SecondaryText)
                        }
                        IconButton(onClick = onNavigateToRoleSelection) {
                            Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = "Switch Role", tint = Primary)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    HorizontalDivider(color = Border)
                    Spacer(modifier = Modifier.height(14.dp))

                    // EV Impact Statistics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Sessions", fontSize = 12.sp, color = SecondaryText)
                            Text("${uiState.totalSessionsCount}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MainText)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Energy Dispensed", fontSize = 12.sp, color = SecondaryText)
                            Text("${uiState.totalEnergyDispensedKwh} kWh", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Primary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("CO2 Offset", fontSize = 12.sp, color = SecondaryText)
                            Text("${uiState.co2SavedKg} kg", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Success)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation Section
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("Ecosystem Shortcuts", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SecondaryText)
                Spacer(modifier = Modifier.height(8.dp))

                ProfileMenuItem(icon = Icons.Default.DirectionsCar, title = "My Electric Vehicles", subtitle = "View garage and connector compatibility", onClick = onNavigateToVehicles)
                ProfileMenuItem(icon = Icons.Default.AccountBalanceWallet, title = "Spacetime Wallet", subtitle = "Balance, payments & ledger history", onClick = onNavigateToWallet)
                ProfileMenuItem(icon = Icons.Default.Bookmark, title = "Saved Charging Stations", subtitle = "${uiState.savedStations.size} favorite locations", onClick = onNavigateToSavedStations)
                ProfileMenuItem(icon = Icons.Default.Notifications, title = "Notifications & Alerts", subtitle = "${uiState.notifications.count { !it.isRead }} unread updates", onClick = onNavigateToNotifications)

                Spacer(modifier = Modifier.height(16.dp))
                Text("Support & Preferences", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SecondaryText)
                Spacer(modifier = Modifier.height(8.dp))

                ProfileMenuItem(icon = Icons.AutoMirrored.Filled.Help, title = "Help & Support Tickets", subtitle = "Disputes, refunds & charger troubleshooting", onClick = onNavigateToHelp)
                ProfileMenuItem(icon = Icons.Default.Language, title = "Language Preference", subtitle = uiState.selectedLanguage, onClick = {})
                ProfileMenuItem(icon = Icons.Default.Security, title = "Privacy & Consent Settings", subtitle = "Data protection & terms", onClick = {})

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, Border),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.logout(onLogout) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Error)
                        Spacer(modifier = Modifier.width(14.dp))
                        Text("Log Out of Account", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Error)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, Border),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Primary.copy(alpha = 0.08f),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MainText)
                Text(text = subtitle, fontSize = 12.sp, color = SecondaryText)
            }
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = SecondaryText, modifier = Modifier.size(16.dp))
        }
    }
}
