package com.spacetime.evcharging.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.spacetime.evcharging.core.navigation.*
import com.spacetime.evcharging.feature.auth.onboarding.OnboardingScreen
import com.spacetime.evcharging.feature.auth.otp.OtpVerificationScreen
import com.spacetime.evcharging.feature.auth.roleselection.RoleSelectionScreen
import com.spacetime.evcharging.feature.auth.splash.SplashScreen
import com.spacetime.evcharging.feature.auth.welcome.WelcomeScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    onNavigateToDashboard: (Any) -> Unit
) {
    composable<SplashRoute> {
        SplashScreen(
            onNavigate = { route ->
                if (route is WelcomeRoute || route is RoleSelectionRoute) {
                    navController.navigate(route) {
                        popUpTo<SplashRoute> { inclusive = true }
                    }
                } else {
                    onNavigateToDashboard(route)
                }
            }
        )
    }

    composable<WelcomeRoute> {
        WelcomeScreen(
            onNavigateToOtp = { challengeId, maskedNumber ->
                navController.navigate(OtpVerificationRoute(challengeId, maskedNumber))
            }
        )
    }

    composable<OtpVerificationRoute> {
        OtpVerificationScreen(
            onNavigate = { route ->
                if (route is OnboardingRoute || route is RoleSelectionRoute) {
                    navController.navigate(route) {
                        popUpTo<WelcomeRoute> { inclusive = true }
                    }
                } else {
                    onNavigateToDashboard(route)
                }
            },
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<OnboardingRoute> {
        OnboardingScreen(
            onNavigateToHome = { onNavigateToDashboard(CustomerShellRoute) }
        )
    }

    composable<RoleSelectionRoute> {
        RoleSelectionScreen(
            onNavigate = { route -> onNavigateToDashboard(route) }
        )
    }
}
