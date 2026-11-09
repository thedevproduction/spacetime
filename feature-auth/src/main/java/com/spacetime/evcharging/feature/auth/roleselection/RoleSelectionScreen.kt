package com.spacetime.evcharging.feature.auth.roleselection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.LoadingScreen
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
        LoadingScreen(message = "Loading roles...")
        return
    }

    Scaffold(
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Welcome, ${uiState.userName}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MainText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Select your workspace to continue",
                fontSize = 14.sp,
                color = SecondaryText
            )

            Spacer(modifier = Modifier.height(32.dp))

            uiState.availableRoles.forEach { roleOption ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { viewModel.selectRole(roleOption.role) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, Border),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Primary.copy(alpha = 0.12f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = roleOption.icon,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = roleOption.displayName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MainText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = roleOption.description,
                                fontSize = 13.sp,
                                color = SecondaryText
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Select",
                            tint = SecondaryText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
