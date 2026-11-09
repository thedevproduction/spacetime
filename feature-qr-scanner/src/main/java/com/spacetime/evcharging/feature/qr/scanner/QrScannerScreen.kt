package com.spacetime.evcharging.feature.qr.scanner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(
    onNavigateBack: () -> Unit,
    onNavigateToQuote: (String) -> Unit,
    viewModel: QrScannerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.resolvedQuote) {
        uiState.resolvedQuote?.let { quote ->
            viewModel.clearResolvedQuote()
            onNavigateToQuote(quote.quoteId)
        }
    }

    Scaffold(
        containerColor = Color.Black
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Camera Viewfinder Simulator
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF111827)),
                contentAlignment = Alignment.Center
            ) {
                // Scanning Target Frame
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .border(3.dp, Primary, RoundedCornerShape(24.dp))
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.isResolving) {
                        CircularProgressIndicator(color = Primary, strokeWidth = 3.dp)
                    } else {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.3f),
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }
            }

            // Top Bar Overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Scan Charger QR",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(
                    onClick = viewModel::toggleTorch,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = if (uiState.isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                        contentDescription = "Flashlight",
                        tint = if (uiState.isTorchOn) Warning else Color.White
                    )
                }
            }

            // Bottom Actions & Guide
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.75f))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Point camera at the QR code on the charging station",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )

                if (uiState.error != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = uiState.error ?: "",
                        fontSize = 13.sp,
                        color = Error,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.showManualInputDialog(true) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Enter Code Manually")
                    }

                    Button(
                        onClick = { viewModel.resolveQr("EVCHARGE:CHG-01-DC") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !uiState.isResolving
                    ) {
                        Text("Simulate Scan")
                    }
                }
            }

            // Manual Code Input Dialog
            if (uiState.isManualInputDialogVisible) {
                AlertDialog(
                    onDismissRequest = { viewModel.showManualInputDialog(false) },
                    title = { Text("Enter Charger ID", fontWeight = FontWeight.Bold, color = MainText) },
                    text = {
                        Column {
                            Text("Type the 6 to 10 character charger code printed under the QR sticker.", fontSize = 13.sp, color = SecondaryText)
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedInput(
                                value = uiState.manualChargerId,
                                onValueChange = viewModel::onManualChargerIdChanged,
                                label = "e.g. CHG-01-DC"
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = viewModel::resolveManualId,
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text("Submit")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.showManualInputDialog(false) }) {
                            Text("Cancel", color = SecondaryText)
                        }
                    },
                    containerColor = CardBackground
                )
            }
        }
    }
}
