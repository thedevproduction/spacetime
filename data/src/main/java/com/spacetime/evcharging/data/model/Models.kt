package com.spacetime.evcharging.data.model

import com.spacetime.evcharging.core.common.model.*
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val fullName: String,
    val mobileNumber: String,
    val email: String? = null,
    val profilePhotoUrl: String? = null,
    val roles: List<UserRole> = listOf(UserRole.CUSTOMER),
    val defaultRole: UserRole = UserRole.CUSTOMER,
    val preferredLanguage: String = "en-IN"
)

data class SendOtpResult(
    val challengeId: String,
    val maskedMobileNumber: String,
    val expiresInSeconds: Int,
    val resendAfterSeconds: Int
)

data class LoginResult(
    val user: User,
    val isNewUser: Boolean
)

data class Station(
    val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val distanceKm: Double,
    val isOpen: Boolean,
    val availability: ChargerStatus,
    val availableConnectors: Int,
    val totalConnectors: Int,
    val minimumTariff: Money,
    val rating: Float,
    val imageUrl: String? = null,
    val amenities: List<String> = emptyList(),
    val operatingHours: String = "24 Hours"
)

data class Charger(
    val chargerId: String,
    val displayName: String,
    val ratedPowerKw: Double,
    val chargerType: String,
    val status: ChargerStatus,
    val connectors: List<Connector>
)

data class Connector(
    val connectorId: Int,
    val connectorType: String,
    val status: ChargerStatus,
    val tariffId: String? = null
)

data class ChargingQuote(
    val quoteId: String,
    val stationId: String,
    val chargerId: String,
    val connectorId: Int,
    val energyRatePerKwh: Money,
    val sessionFee: Money,
    val estimatedTotal: Money,
    val walletAvailable: Money
)

data class ActiveChargingSession(
    val sessionId: String,
    val status: SessionStatus,
    val stationName: String,
    val chargerId: String,
    val connectorId: Int,
    val startedAt: String,
    val energyWh: Long,
    val energyKwh: Double,
    val powerKw: Double,
    val estimatedAmount: Money,
    val durationSeconds: Long = 0
)

data class Wallet(
    val walletId: String,
    val availableBalance: Money,
    val reservedBalance: Money,
    val promotionalBalance: Money
)

data class WalletTransaction(
    val transactionId: String,
    val type: String,
    val amount: Money,
    val status: String,
    val relatedSessionId: String? = null,
    val reference: String? = null,
    val timestamp: String
)

data class SiteAdminDashboard(
    val grossRevenue: Money,
    val energyDeliveredWh: Long,
    val sessionCount: Int,
    val uniqueCustomerCount: Int,
    val availableConnectors: Int,
    val occupiedConnectors: Int,
    val offlineConnectors: Int,
    val faultedConnectors: Int
)

data class ProviderDashboard(
    val totalCustomers: Int,
    val totalInstallations: Int,
    val totalChargersInstalled: Int,
    val pendingInstallations: Int,
    val activeInstallations: Int,
    val openServiceTickets: Int,
    val warrantyExpiring: Int,
    val amcExpiring: Int
)

data class Vehicle(
    val id: String,
    val nickname: String,
    val category: String,
    val manufacturer: String,
    val model: String,
    val registrationNumber: String? = null,
    val batteryCapacityKwh: Double = 40.0,
    val connectorTypes: List<String> = listOf("CCS2"),
    val maxAcPowerKw: Double = 7.2,
    val maxDcPowerKw: Double = 50.0,
    val isDefault: Boolean = false
)

data class SessionSummary(
    val sessionId: String,
    val stationName: String,
    val chargerId: String,
    val connectorId: Int,
    val totalEnergyKwh: Double,
    val durationFormatted: String,
    val startTime: String,
    val endTime: String,
    val tariffRate: Money,
    val sessionFee: Money,
    val parkingFee: Money,
    val taxAmount: Money,
    val discountAmount: Money,
    val finalAmount: Money,
    val paymentMethod: String,
    val paymentStatus: PaymentStatus = PaymentStatus.CAPTURED,
    val invoiceUrl: String? = null
)

data class SiteAdminRevenue(
    val grossChargingValue: Money,
    val platformCommission: Money,
    val gatewayFees: Money,
    val taxAmount: Money,
    val netSettlement: Money,
    val paidSettlements: Money,
    val pendingSettlements: Money
)

data class SettlementRecord(
    val settlementId: String,
    val period: String,
    val grossAmount: Money,
    val deductions: Money,
    val netAmount: Money,
    val status: String,
    val expectedDate: String,
    val paidDate: String? = null
)

data class SiteLiveSession(
    val sessionId: String,
    val stationName: String,
    val chargerId: String,
    val connectorId: Int,
    val customerMaskedMobile: String,
    val energyKwh: Double,
    val powerKw: Double,
    val estimatedAmount: Money,
    val startedAt: String,
    val status: SessionStatus = SessionStatus.CHARGING
)

data class SiteEmployee(
    val id: String,
    val name: String,
    val mobileNumber: String,
    val role: String,
    val assignedSite: String,
    val status: String = "ACTIVE"
)

data class InstallationProject(
    val id: String,
    val customerName: String,
    val siteName: String,
    val address: String,
    val chargerCount: Int,
    val scheduledDate: String,
    val status: String, // SURVEY_PENDING, QUOTATION_SUBMITTED, INSTALLATION_IN_PROGRESS, COMMISSIONING_PENDING, COMPLETED
    val technicianName: String? = null,
    val electricalSurveyDone: Boolean = false,
    val commissioningDone: Boolean = false
)

data class EquipmentItem(
    val id: String,
    val serialNumber: String,
    val manufacturer: String,
    val model: String,
    val ratedPowerKw: Double,
    val chargerType: String,
    val connectorTypes: List<String>,
    val customerName: String,
    val siteName: String,
    val warrantyExpiryDate: String,
    val status: String // IN_STOCK, DISPATCHED, INSTALLED, UNDER_MAINTENANCE
)

data class ServiceTicket(
    val id: String,
    val customerName: String,
    val stationName: String,
    val chargerId: String,
    val problemDescription: String,
    val priority: String, // HIGH, MEDIUM, LOW
    val slaDueFormatted: String,
    val assignedTechnician: String? = null,
    val status: String // OPEN, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED
)

data class TechnicianProfile(
    val id: String,
    val name: String,
    val mobileNumber: String,
    val serviceRegion: String,
    val skills: List<String>,
    val availability: String = "AVAILABLE",
    val activeTicketsCount: Int = 1
)
