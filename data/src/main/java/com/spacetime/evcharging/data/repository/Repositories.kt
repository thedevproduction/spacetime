package com.spacetime.evcharging.data.repository

import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.common.model.UserRole
import com.spacetime.evcharging.data.model.*

interface AuthRepository {
    suspend fun sendOtp(countryCode: String, mobileNumber: String): AppResult<SendOtpResult>
    suspend fun verifyOtp(challengeId: String, otp: String): AppResult<LoginResult>
    suspend fun refreshToken(): AppResult<Unit>
    suspend fun logout(logoutAll: Boolean = false): AppResult<Unit>
    suspend fun getCurrentUser(): AppResult<User>
    suspend fun switchRole(role: UserRole, scopeId: String? = null): AppResult<User>
    suspend fun updateProfile(name: String, email: String?, city: String?, language: String): AppResult<User>
    suspend fun isLoggedIn(): Boolean
}

interface StationRepository {
    suspend fun getNearbyStations(lat: Double, lng: Double, radiusKm: Double = 25.0): AppResult<List<Station>>
    suspend fun getStationDetails(stationId: String): AppResult<Station>
    suspend fun getStationChargers(stationId: String): AppResult<List<Charger>>
    suspend fun searchStations(query: String): AppResult<List<Station>>
}

interface ChargingRepository {
    suspend fun resolveQr(qrPayload: String): AppResult<ChargingQuote>
    suspend fun getQuote(quoteId: String): AppResult<ChargingQuote>
    suspend fun createQuote(stationId: String, chargerId: String, connectorId: Int): AppResult<ChargingQuote>
    suspend fun startSession(quoteId: String, paymentMode: String = "WALLET"): AppResult<String>
    suspend fun getActiveSession(): AppResult<ActiveChargingSession?>
    suspend fun stopSession(sessionId: String): AppResult<SessionSummary>
    suspend fun getSessionSummary(sessionId: String): AppResult<SessionSummary>
}

interface WalletRepository {
    suspend fun getWallet(): AppResult<Wallet>
    suspend fun createTopUp(amountMinor: Long): AppResult<String>
    suspend fun getTransactions(): AppResult<List<WalletTransaction>>
}

interface SiteAdminRepository {
    suspend fun getDashboard(siteId: String? = null): AppResult<SiteAdminDashboard>
    suspend fun getStations(): AppResult<List<Station>>
    suspend fun getRevenueSummary(): AppResult<SiteAdminRevenue>
    suspend fun getSettlements(): AppResult<List<SettlementRecord>>
    suspend fun getLiveSessions(): AppResult<List<SiteLiveSession>>
    suspend fun getEmployees(): AppResult<List<SiteEmployee>>
    suspend fun inviteEmployee(name: String, mobile: String, role: String, siteId: String): AppResult<SiteEmployee>
}

interface ServiceProviderRepository {
    suspend fun getDashboard(): AppResult<ProviderDashboard>
    suspend fun getInstallations(): AppResult<List<InstallationProject>>
    suspend fun createInstallation(project: InstallationProject): AppResult<InstallationProject>
    suspend fun updateInstallationStatus(id: String, status: String): AppResult<Unit>
    suspend fun getEquipment(): AppResult<List<EquipmentItem>>
    suspend fun registerEquipment(item: EquipmentItem): AppResult<EquipmentItem>
    suspend fun getServiceTickets(): AppResult<List<ServiceTicket>>
    suspend fun assignTicket(ticketId: String, technicianName: String): AppResult<Unit>
    suspend fun resolveTicket(ticketId: String, resolutionNotes: String): AppResult<Unit>
    suspend fun getTechnicians(): AppResult<List<TechnicianProfile>>
}

interface VehicleRepository {
    suspend fun getVehicles(): AppResult<List<Vehicle>>
    suspend fun addVehicle(vehicle: Vehicle): AppResult<Vehicle>
    suspend fun deleteVehicle(vehicleId: String): AppResult<Unit>
    suspend fun setDefaultVehicle(vehicleId: String): AppResult<Unit>
}
