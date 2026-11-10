package com.spacetime.evcharging.feature.qr.scanner

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
    SystemBarAppearance(isDarkBackground = true)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.resolvedQuote) {
        uiState.resolvedQuote?.let { quote ->
            viewModel.clearResolvedQuote()
            onNavigateToQuote(quote.quoteId)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "scanLaser")
    val laserProgress by infiniteTransition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.92f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laserY"
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = Color(0xFF090D16)
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
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0B1120),
                                Color(0xFF111827),
                                Color(0xFF090D16)
                            )
                        )
                    )
                    .padding(bottom = 140.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.size(260.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Corner brackets & animated scan line
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeW = 4.dp.toPx()
                        val cornerLen = 36.dp.toPx()
                        val radius = 20.dp.toPx()

                        // Subtle frame outline
                        drawRoundRect(
                            color = Color.White.copy(alpha = 0.14f),
                            cornerRadius = CornerRadius(radius, radius),
                            style = Stroke(width = 1.5.dp.toPx())
                        )

                        // Corner accents (Top-Left, Top-Right, Bottom-Left, Bottom-Right)
                        val bracketColor = PrimaryLight
                        // Top-left
                        drawLine(bracketColor, Offset(0f, cornerLen), Offset(0f, radius), strokeW, StrokeCap.Round)
                        drawLine(bracketColor, Offset(radius, 0f), Offset(cornerLen, 0f), strokeW, StrokeCap.Round)
                        // Top-right
                        drawLine(bracketColor, Offset(size.width - cornerLen, 0f), Offset(size.width - radius, 0f), strokeW, StrokeCap.Round)
                        drawLine(bracketColor, Offset(size.width, radius), Offset(size.width, cornerLen), strokeW, StrokeCap.Round)
                        // Bottom-left
                        drawLine(bracketColor, Offset(0f, size.height - cornerLen), Offset(0f, size.height - radius), strokeW, StrokeCap.Round)
                        drawLine(bracketColor, Offset(radius, size.height), Offset(cornerLen, size.height), strokeW, StrokeCap.Round)
                        // Bottom-right
                        drawLine(bracketColor, Offset(size.width - cornerLen, size.height), Offset(size.width - radius, size.height), strokeW, StrokeCap.Round)
                        drawLine(bracketColor, Offset(size.width, size.height - cornerLen), Offset(size.width, size.height - radius), strokeW, StrokeCap.Round)

                        // Animated laser scan bar
                        if (!uiState.isResolving) {
                            val yPos = size.height * laserProgress
                            drawRect(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        PrimaryLight.copy(alpha = 0.85f),
                                        Color.Transparent
                                    )
                                ),
                                topLeft = Offset(16.dp.toPx(), yPos),
                                size = Size(size.width - 32.dp.toPx(), 2.5.dp.toPx())
                            )
                        }
                    }

                    if (uiState.isResolving) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = PrimaryLight, strokeWidth = 3.dp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Identifying charger...",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(110.dp)
                        )
                    }
                }
            }

            // Top Bar Overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.14f),
                    modifier = Modifier.size(42.dp)
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Scan Charger QR",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Instant plug & charge",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = if (uiState.isTorchOn) Warning.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.14f),
                    modifier = Modifier.size(42.dp)
                ) {
                    IconButton(onClick = viewModel::toggleTorch) {
                        Icon(
                            imageVector = if (uiState.isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                            contentDescription = "Flashlight",
                            tint = if (uiState.isTorchOn) Warning else Color.White
                        )
                    }
                }
            }

            // Bottom Sheet Controls
            Surface(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Color(0xFF151C2C),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 22.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White.copy(alpha = 0.22f))
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Align the QR sticker on the charger inside the frame",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.88f),
                        textAlign = TextAlign.Center
                    )

                    if (uiState.error != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Error.copy(alpha = 0.16f)
                        ) {
                            Text(
                                text = uiState.error ?: "",
                                fontSize = 12.sp,
                                color = Color(0xFFFCA5A5),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.showManualInputDialog(true) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Keyboard, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Enter ID", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }

                        Button(
                            onClick = { viewModel.resolveQr("EVCHARGE:CHG-01-DC") },
                            modifier = Modifier
                                .weight(1.2f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryLight),
                            shape = RoundedCornerShape(14.dp),
                            enabled = !uiState.isResolving
                        ) {
                            Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Simulate Scan", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
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
