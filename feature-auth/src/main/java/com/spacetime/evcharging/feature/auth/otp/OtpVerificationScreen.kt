package com.spacetime.evcharging.feature.auth.otp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*
import com.spacetime.evcharging.core.navigation.NavigationEvent

@Composable
fun OtpVerificationScreen(
    onNavigate: (Any) -> Unit,
    onBackClick: () -> Unit,
    viewModel: OtpVerificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.Navigate -> onNavigate(event.route)
                is NavigationEvent.NavigateAndClearStack -> onNavigate(event.route)
                NavigationEvent.NavigateBack -> onBackClick()
            }
        }
    }

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Verify your number",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MainText
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "We sent a 6-digit code to ${uiState.maskedPhoneNumber}",
                fontSize = 14.sp,
                color = SecondaryText
            )

            Spacer(modifier = Modifier.height(32.dp))

            OtpInput(
                length = 6,
                value = uiState.otp,
                onValueChange = viewModel::onOtpChanged,
                error = uiState.error
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uiState.canResend) {
                    TextButton(
                        text = "Resend OTP",
                        onClick = viewModel::resendOtp
                    )
                } else {
                    Text(
                        text = "Resend in ${uiState.resendCountdownSeconds}s",
                        fontSize = 13.sp,
                        color = SecondaryText
                    )
                }

                TextButton(
                    text = "Change Number",
                    onClick = onBackClick,
                    color = SecondaryText
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Verify & Proceed",
                onClick = viewModel::verifyOtp,
                enabled = uiState.isOtpComplete,
                isLoading = uiState.isLoading
            )
        }
    }
}
