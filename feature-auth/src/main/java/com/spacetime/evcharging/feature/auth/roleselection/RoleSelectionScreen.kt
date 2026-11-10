package com.spacetime.evcharging.feature.auth.roleselection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.LoadingScreen
import com.spacetime.evcharging.core.designsystem.component.SpacetimeBrandLogo
import com.spacetime.evcharging.core.designsystem.theme.*
import com.spacetime.evcharging.core.navigation.NavigationEvent

@Composable
fun RoleSelectionScreen(
    onNavigate: (Any) -> Unit,
    viewModel: RoleSelectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            if (event is NavigationEvent.NavigateAndClearStack) {
                onNavigate(event.route)
            }
        }
    }

    if (uiState.isLoading && uiState.availableRoles.isEmpty()) {
        LoadingScreen(message = "Loading workspaces...")
        return
    }

    Scaffold(
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))

                // Brand Identity Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SpacetimeBrandLogo(
                        size = 42.dp,
                        cornerRadius = 12.dp
                    )
                    Column {
                        Text(
                            text = "SPACETIME ENERGY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp,
                            color = Primary
                        )
                        Text(
                            text = "Smart EV Charging Network",
                            fontSize = 12.sp,
                            color = SecondaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Welcome back,\n${uiState.userName}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 32.sp,
                    color = MainText
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Select the workspace you want to open",
                    fontSize = 14.sp,
                    color = SecondaryText
                )

                Spacer(modifier = Modifier.height(28.dp))

                uiState.availableRoles.forEach { roleOption ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 7.dp)
                            .clickable { viewModel.selectRole(roleOption.role) },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, Border),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(15.dp),
                                color = roleOption.accentColor.copy(alpha = 0.12f),
                                modifier = Modifier.size(54.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = roleOption.icon,
                                        contentDescription = null,
                                        tint = roleOption.accentColor,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = roleOption.accentColor.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        text = roleOption.badge,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.6.sp,
                                        color = roleOption.accentColor,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = roleOption.displayName,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MainText
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = roleOption.description,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp,
                                    color = SecondaryText
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Surface(
                                shape = CircleShape,
                                color = AppBackground,
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Select",
                                        tint = roleOption.accentColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Footer helper banner
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CardBackground,
                border = BorderStroke(1.dp, Border),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "You can switch workspaces anytime from your profile settings.",
                        fontSize = 12.sp,
                        color = SecondaryText
                    )
                }
            }
        }
    }
}
