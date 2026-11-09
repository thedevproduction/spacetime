package com.spacetime.evcharging.core.navigation

import kotlinx.serialization.Serializable

// Authentication routes
@Serializable data object SplashRoute
@Serializable data object WelcomeRoute
@Serializable data class OtpVerificationRoute(val challengeId: String, val maskedNumber: String)
@Serializable data object OnboardingRoute
@Serializable data object RoleSelectionRoute

// Role Shell Routes
@Serializable data object CustomerShellRoute
@Serializable data object SiteAdminShellRoute
@Serializable data object ProviderShellRoute

// Customer routes
@Serializable data object CustomerHomeRoute
@Serializable data object ExploreMapRoute
@Serializable data object StationListRoute
@Serializable data class StationDetailsRoute(val stationId: String)
@Serializable data class ChargerSelectionRoute(val stationId: String)
@Serializable data object QrScannerRoute
@Serializable data class ChargingQuoteRoute(val quoteId: String)
@Serializable data class PaymentSelectionRoute(val quoteId: String)
@Serializable data class ChargingStartRoute(val sessionId: String)
@Serializable data class ActiveChargingRoute(val sessionId: String)
@Serializable data class ChargingSummaryRoute(val sessionId: String)
@Serializable data object ChargingHistoryRoute
@Serializable data object WalletRoute
@Serializable data object AddMoneyRoute
@Serializable data object WalletTransactionsRoute
@Serializable data class WalletTransactionDetailRoute(val transactionId: String)
@Serializable data object VehiclesRoute
@Serializable data object AddVehicleRoute
@Serializable data class EditVehicleRoute(val vehicleId: String)
@Serializable data object SavedStationsRoute
@Serializable data object NotificationsRoute
@Serializable data object CustomerProfileRoute
@Serializable data object HelpSupportRoute

// Site Admin routes
@Serializable data object SiteAdminDashboardRoute
@Serializable data object SiteStationListRoute
@Serializable data class SiteStationDetailsRoute(val stationId: String)
@Serializable data class SiteChargerManagementRoute(val stationId: String)
@Serializable data class SiteChargerDetailsRoute(val chargerId: String)
@Serializable data object SiteLiveSessionsRoute
@Serializable data class SiteSessionDetailsRoute(val sessionId: String)
@Serializable data object SiteCustomerActivityRoute
@Serializable data object SiteRevenueDashboardRoute
@Serializable data object SiteSettlementHistoryRoute
@Serializable data class SiteSettlementDetailsRoute(val settlementId: String)
@Serializable data class SitePricingManagementRoute(val stationId: String)
@Serializable data class SiteOperatingHoursRoute(val stationId: String)
@Serializable data object SiteEmployeesRoute
@Serializable data object SiteReportsRoute
@Serializable data object SiteMaintenanceRequestsRoute
@Serializable data object SiteAdminProfileRoute

// Service Provider routes
@Serializable data object ProviderDashboardRoute
@Serializable data object ProviderCustomerListRoute
@Serializable data class ProviderCustomerDetailsRoute(val customerId: String)
@Serializable data object ProviderInstallationListRoute
@Serializable data object ProviderCreateInstallationRoute
@Serializable data class ProviderInstallationDetailsRoute(val installationId: String)
@Serializable data class ProviderSiteSurveyRoute(val installationId: String)
@Serializable data class ProviderQuotationDetailsRoute(val installationId: String)
@Serializable data object ProviderEquipmentListRoute
@Serializable data class ProviderEquipmentDetailsRoute(val equipmentId: String)
@Serializable data object ProviderMaintenanceTicketsRoute
@Serializable data class ProviderTicketDetailsRoute(val ticketId: String)
@Serializable data object ProviderTechnicianListRoute
@Serializable data object ProviderWarrantyAmcRoute
@Serializable data object ProviderSalesReportsRoute
@Serializable data object ProviderProfileRoute
