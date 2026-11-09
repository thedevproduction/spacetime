package com.spacetime.evcharging.feature.auth.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.theme.*
import com.spacetime.evcharging.core.navigation.NavigationEvent

@Composable
fun SplashScreen(
    onNavigate: (Any) -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.Navigate -> onNavigate(event.route)
                is NavigationEvent.NavigateAndClearStack -> onNavigate(event.route)
                NavigationEvent.NavigateBack -> {}
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Secondary, Color(0xFF0A1929))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(Primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SE",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Spacetime Energy",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "FUTURES",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 4.sp,
                color = Primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Find. Charge. Drive.",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            CircularProgressIndicator(
                color = Primary,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
