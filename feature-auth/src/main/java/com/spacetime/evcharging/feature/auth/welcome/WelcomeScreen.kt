package com.spacetime.evcharging.feature.auth.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.PhoneNumberInput
import com.spacetime.evcharging.core.designsystem.component.PrimaryButton
import com.spacetime.evcharging.core.designsystem.component.SpacetimeBrandLogo
import com.spacetime.evcharging.core.designsystem.theme.*
import com.spacetime.evcharging.core.navigation.NavigationEvent

@Composable
fun WelcomeScreen(
    onNavigateToOtp: (String, String) -> Unit,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            if (event is NavigationEvent.Navigate) {
                val route = event.route as com.spacetime.evcharging.core.navigation.OtpVerificationRoute
                onNavigateToOtp(route.challengeId, route.maskedNumber)
            }
        }
    }

    Scaffold(
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            SpacetimeBrandLogo(
                size = 76.dp,
                cornerRadius = 22.dp,
                showGlowBorder = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Spacetime Energy Futures",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MainText
            )
            Text(
                text = "India's Smart EV Charging Network",
                fontSize = 13.sp,
                color = SecondaryText
            )

            Spacer(modifier = Modifier.height(48.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Welcome",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MainText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Enter your mobile number to get started",
                    fontSize = 14.sp,
                    color = SecondaryText
                )

                Spacer(modifier = Modifier.height(24.dp))

                PhoneNumberInput(
                    countryCode = uiState.countryCode,
                    phoneNumber = uiState.phoneNumber,
                    onPhoneChange = viewModel::onPhoneNumberChanged,
                    error = uiState.error
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Continue",
                    onClick = viewModel::sendOtp,
                    enabled = uiState.isPhoneValid,
                    isLoading = uiState.isLoading
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "By continuing, you agree to our Terms of Service & Privacy Policy",
                fontSize = 12.sp,
                color = SecondaryText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}
