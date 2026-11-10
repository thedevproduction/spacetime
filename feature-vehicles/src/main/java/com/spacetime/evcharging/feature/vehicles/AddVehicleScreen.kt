package com.spacetime.evcharging.feature.vehicles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun AddVehicleScreen(
    onNavigateBack: () -> Unit,
    viewModel: VehiclesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            viewModel.resetSaveState()
            onNavigateBack()
        }
    }

    val popularMakers = listOf("Tata Motors", "MG", "Mahindra", "Hyundai", "BYD")
    val availableConnectors = listOf("CCS2", "Type 2", "GB/T", "CHAdeMO")

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Add Electric Vehicle",
                showBack = true,
                onBackClick = onNavigateBack
            )
        },
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Vehicle Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MainText
            )
            Text(
                text = "Helps us filter compatible charging points for your car.",
                fontSize = 13.sp,
                color = SecondaryText
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Manufacturer", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MainText)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                popularMakers.forEach { maker ->
                    val isSelected = uiState.manufacturer == maker
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Primary else CardBackground,
                        border = BorderStroke(1.dp, if (isSelected) Primary else Border),
                        modifier = Modifier.clickable { viewModel.onManufacturerChanged(maker) }
                    ) {
                        Text(
                            text = maker,
                            fontSize = 12.sp,
                            maxLines = 1,
                            softWrap = false,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) Color.White else MainText,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedInput(
                value = uiState.model,
                onValueChange = viewModel::onModelChanged,
                label = "Model Name *"
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedInput(
                value = uiState.nickname,
                onValueChange = viewModel::onNicknameChanged,
                label = "Nickname (e.g. My Road-tripper)"
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedInput(
                value = uiState.regNumber,
                onValueChange = viewModel::onRegNumberChanged,
                label = "Registration Number (Optional, e.g. MP20AB1234)"
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedInput(
                value = uiState.batteryCapacityKwh,
                onValueChange = viewModel::onBatteryCapacityChanged,
                label = "Battery Capacity (kWh) *",
                keyboardType = KeyboardType.Decimal
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Supported Connectors", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MainText)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableConnectors.forEach { conn ->
                    val isSelected = uiState.selectedConnectors.contains(conn)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Primary.copy(alpha = 0.15f) else CardBackground,
                        border = BorderStroke(1.5.dp, if (isSelected) Primary else Border),
                        modifier = Modifier.clickable { viewModel.toggleConnector(conn) }
                    ) {
                        Text(
                            text = conn,
                            fontSize = 13.sp,
                            maxLines = 1,
                            softWrap = false,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) Primary else MainText,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Save Vehicle",
                onClick = viewModel::saveVehicle,
                enabled = uiState.model.isNotBlank() && uiState.selectedConnectors.isNotEmpty(),
                isLoading = uiState.isSaving
            )

            Spacer(modifier = Modifier.navigationBarsPadding().height(24.dp))
        }
    }
}
