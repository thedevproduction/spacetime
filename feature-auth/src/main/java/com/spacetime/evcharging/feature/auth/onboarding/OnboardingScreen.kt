package com.spacetime.evcharging.feature.auth.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.OutlinedInput
import com.spacetime.evcharging.core.designsystem.component.PrimaryButton
import com.spacetime.evcharging.core.designsystem.component.SpacetimeTopBar
import com.spacetime.evcharging.core.designsystem.theme.*
import com.spacetime.evcharging.core.navigation.NavigationEvent

@Composable
fun OnboardingScreen(
    onNavigateToHome: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            if (event is NavigationEvent.NavigateAndClearStack) {
                onNavigateToHome()
            }
        }
    }

    Scaffold(
        topBar = { SpacetimeTopBar(title = "Create Account") },
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
                text = "Welcome to Spacetime!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MainText
            )
            Text(
                text = "Complete your profile to discover nearby chargers.",
                fontSize = 14.sp,
                color = SecondaryText
            )

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedInput(
                value = uiState.fullName,
                onValueChange = viewModel::onNameChanged,
                label = "Full Name *",
                leadingIcon = Icons.Default.Person,
                error = uiState.nameError
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedInput(
                value = uiState.email,
                onValueChange = viewModel::onEmailChanged,
                label = "Email Address (Optional)",
                leadingIcon = Icons.Default.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedInput(
                value = uiState.city,
                onValueChange = viewModel::onCityChanged,
                label = "City (e.g. Jabalpur, Bhopal)",
                leadingIcon = Icons.Default.LocationCity
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CardBackground,
                border = BorderStroke(1.dp, if (uiState.termsAccepted) Primary else Border),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.onTermsAcceptedChanged(!uiState.termsAccepted) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Checkbox(
                        checked = uiState.termsAccepted,
                        onCheckedChange = viewModel::onTermsAcceptedChanged,
                        colors = CheckboxDefaults.colors(checkedColor = Primary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "I accept the Terms of Service & Privacy Policy",
                        fontSize = 13.sp,
                        color = MainText
                    )
                }
            }

            if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "",
                    color = Error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Complete Setup",
                onClick = viewModel::createAccount,
                enabled = uiState.fullName.isNotBlank() && uiState.termsAccepted,
                isLoading = uiState.isLoading
            )

            Spacer(modifier = Modifier.navigationBarsPadding().height(24.dp))
        }
    }
}
