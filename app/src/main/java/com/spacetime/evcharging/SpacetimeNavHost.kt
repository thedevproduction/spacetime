package com.spacetime.evcharging

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.spacetime.evcharging.core.navigation.*
import com.spacetime.evcharging.feature.auth.navigation.authNavGraph
import com.spacetime.evcharging.feature.charging.ActiveChargingScreen
import com.spacetime.evcharging.feature.charging.ChargingQuoteScreen
import com.spacetime.evcharging.feature.charging.ChargingStartConfirmationScreen
import com.spacetime.evcharging.feature.charging.ChargingSummaryScreen
import com.spacetime.evcharging.feature.wallet.AddMoneyScreen
import com.spacetime.evcharging.feature.wallet.WalletTransactionsScreen
import com.spacetime.evcharging.feature.qr.scanner.QrScannerScreen
import com.spacetime.evcharging.feature.station.details.ChargerSelectionScreen
import com.spacetime.evcharging.feature.station.details.StationDetailsScreen
import com.spacetime.evcharging.feature.vehicles.AddVehicleScreen
import com.spacetime.evcharging.feature.vehicles.VehicleListScreen
import com.spacetime.evcharging.navigation.CustomerShell
import com.spacetime.evcharging.navigation.ProviderShell
import com.spacetime.evcharging.navigation.SiteAdminShell

@Composable
fun SpacetimeNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SplashRoute
    ) {
        authNavGraph(
            navController = navController,
            onNavigateToDashboard = { targetRoute ->
                navController.navigate(targetRoute) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )

        // Customer Shell
        composable<CustomerShellRoute> {
            CustomerShell(rootNavController = navController)
        }

        // Station Details & Charger Selection (Milestone 2)
        composable<StationDetailsRoute> {
            StationDetailsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChargerSelection = { stationId ->
                    navController.navigate(ChargerSelectionRoute(stationId))
                }
            )
        }

        composable<ChargerSelectionRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ChargerSelectionRoute>()
            ChargerSelectionScreen(
                stationId = route.stationId,
                onNavigateBack = { navController.popBackStack() },
                onContinueToQuote = { stationId, chargerId, connectorId ->
                    navController.navigate(ChargingQuoteRoute(quoteId = "quote_${System.currentTimeMillis()}"))
                }
            )
        }

        // Wallet & Payments (Milestone 4)
        composable<AddMoneyRoute> {
            AddMoneyScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<WalletTransactionsRoute> {
            WalletTransactionsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Vehicle Management (Milestone 2)
        composable<VehiclesRoute> {
            VehicleListScreen(
                onNavigateToAddVehicle = { navController.navigate(AddVehicleRoute) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<AddVehicleRoute> {
            AddVehicleScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Real Charging Journey (Milestone 3)
        composable<QrScannerRoute> {
            QrScannerScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToQuote = { quoteId ->
                    navController.navigate(ChargingQuoteRoute(quoteId = quoteId))
                }
            )
        }

        composable<ChargingQuoteRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ChargingQuoteRoute>()
            ChargingQuoteScreen(
                quoteId = route.quoteId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToStartConfirm = {
                    navController.navigate(ChargingStartRoute(sessionId = route.quoteId))
                }
            )
        }

        composable<ChargingStartRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ChargingStartRoute>()
            ChargingStartConfirmationScreen(
                quoteId = route.sessionId,
                onNavigateBack = { navController.popBackStack() },
                onChargingStarted = { sessionId ->
                    navController.navigate(ActiveChargingRoute(sessionId = sessionId)) {
                        popUpTo<CustomerShellRoute> { inclusive = false }
                    }
                }
            )
        }

        composable<ActiveChargingRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ActiveChargingRoute>()
            ActiveChargingScreen(
                sessionId = route.sessionId,
                onNavigateToSummary = { sessionId ->
                    navController.navigate(ChargingSummaryRoute(sessionId = sessionId)) {
                        popUpTo<CustomerShellRoute> { inclusive = false }
                    }
                }
            )
        }

        composable<ChargingSummaryRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ChargingSummaryRoute>()
            ChargingSummaryScreen(
                sessionId = route.sessionId,
                onChargeAgain = {
                    navController.navigate(QrScannerRoute) {
                        popUpTo<CustomerShellRoute> { inclusive = false }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(CustomerShellRoute) {
                        popUpTo<CustomerShellRoute> { inclusive = true }
                    }
                }
            )
        }

        // Site Admin Shell
        composable<SiteAdminShellRoute> {
            SiteAdminShell(rootNavController = navController)
        }

        // Service Provider Shell
        composable<ProviderShellRoute> {
            ProviderShell(rootNavController = navController)
        }
    }
}
