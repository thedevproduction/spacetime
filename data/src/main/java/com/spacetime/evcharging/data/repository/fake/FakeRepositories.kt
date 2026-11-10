package com.spacetime.evcharging.data.repository.fake

import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.common.model.*
import com.spacetime.evcharging.core.security.TokenManager
import com.spacetime.evcharging.data.model.*
import com.spacetime.evcharging.data.repository.*
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthRepository @Inject constructor(
    private val tokenManager: TokenManager
) : AuthRepository {

    private var currentUser: User? = User(
        id = "usr_001",
        fullName = "Rahul Sharma",
        mobileNumber = "+919876543210",
        email = "rahul.sharma@example.com",
        roles = listOf(UserRole.CUSTOMER, UserRole.SITE_ADMIN, UserRole.SERVICE_PROVIDER),
        defaultRole = UserRole.CUSTOMER
    )

    override suspend fun sendOtp(countryCode: String, mobileNumber: String): AppResult<SendOtpResult> {
        delay(600)
        return AppResult.Success(
            SendOtpResult(
                challengeId = "ch_fake_123",
                maskedMobileNumber = "$countryCode ******${mobileNumber.takeLast(4)}",
                expiresInSeconds = 300,
                resendAfterSeconds = 30
            )
        )
    }

    override suspend fun verifyOtp(challengeId: String, otp: String): AppResult<LoginResult> {
        delay(800)
        tokenManager.saveTokens("mock_access_token", "mock_refresh_token")
        return AppResult.Success(
            LoginResult(
                user = currentUser ?: User(
                    id = "usr_new",
                    fullName = "New User",
                    mobileNumber = "+919876543210",
                    roles = listOf(UserRole.CUSTOMER),
                    defaultRole = UserRole.CUSTOMER
                ),
                isNewUser = false
            )
        )
    }

    override suspend fun refreshToken(): AppResult<Unit> = AppResult.Success(Unit)

    override suspend fun logout(logoutAll: Boolean): AppResult<Unit> {
        tokenManager.clearTokens()
        return AppResult.Success(Unit)
    }

    override suspend fun getCurrentUser(): AppResult<User> {
        return currentUser?.let { AppResult.Success(it) }
            ?: AppResult.Error(message = "Not logged in")
    }

    override suspend fun switchRole(role: UserRole, scopeId: String?): AppResult<User> {
        val updated = currentUser?.copy(defaultRole = role) ?: return AppResult.Error(message = "Not logged in")
        currentUser = updated
        return AppResult.Success(updated)
    }

    override suspend fun updateProfile(name: String, email: String?, city: String?, language: String): AppResult<User> {
        val updated = currentUser?.copy(fullName = name, email = email, preferredLanguage = language)
            ?: return AppResult.Error(message = "Not logged in")
        currentUser = updated
        return AppResult.Success(updated)
    }

    override suspend fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()
}

@Singleton
class FakeStationRepository @Inject constructor() : StationRepository {

    private val stations = listOf(
        Station(
            id = "st_001",
            name = "Highway EV Superhub Jabalpur",
            address = "NH-34, Bypass Road, Madhotal, Jabalpur",
            latitude = 23.1815,
            longitude = 79.9864,
            distanceKm = 2.4,
            isOpen = true,
            availability = ChargerStatus.AVAILABLE,
            availableConnectors = 4,
            totalConnectors = 6,
            minimumTariff = Money(1400),
            rating = 4.8f,
            amenities = listOf("Restroom", "Cafe", "WiFi", "24/7 Security")
        ),
        Station(
            id = "st_002",
            name = "Civic Centre Fast Charger",
            address = "Marhatal, Civic Centre, Jabalpur",
            latitude = 23.1685,
            longitude = 79.9328,
            distanceKm = 4.8,
            isOpen = true,
            availability = ChargerStatus.AVAILABLE,
            availableConnectors = 2,
            totalConnectors = 4,
            minimumTariff = Money(1500),
            rating = 4.5f,
            amenities = listOf("Shopping", "Food Court")
        ),
        Station(
            id = "st_003",
            name = "Spacetime Green Station Bhopal",
            address = "Hoshangabad Road, MP Nagar, Bhopal",
            latitude = 23.2324,
            longitude = 77.4343,
            distanceKm = 12.0,
            isOpen = true,
            availability = ChargerStatus.CHARGING,
            availableConnectors = 1,
            totalConnectors = 6,
            minimumTariff = Money(1350),
            rating = 4.7f,
            amenities = listOf("Restroom", "Waiting Lounge", "EV Spares")
        ),
        Station(
            id = "st_004",
            name = "Bhedaghat Marble Rocks EV Hub",
            address = "Near Dhuandhar Falls, Bhedaghat, Jabalpur",
            latitude = 23.1310,
            longitude = 79.8005,
            distanceKm = 8.5,
            isOpen = true,
            availability = ChargerStatus.AVAILABLE,
            availableConnectors = 3,
            totalConnectors = 4,
            minimumTariff = Money(1400),
            rating = 4.9f,
            amenities = listOf("Scenic View", "Cafe", "Restroom")
        ),
        Station(
            id = "st_005",
            name = "DB City Mall EV Station Bhopal",
            address = "Arera Hills, MP Nagar Zone-1, Bhopal",
            latitude = 23.2330,
            longitude = 77.4300,
            distanceKm = 1.8,
            isOpen = true,
            availability = ChargerStatus.AVAILABLE,
            availableConnectors = 6,
            totalConnectors = 8,
            minimumTariff = Money(1600),
            rating = 4.6f,
            amenities = listOf("Multiplex", "Food Court", "Covered Parking", "Valet")
        ),
        Station(
            id = "st_006",
            name = "Raja Bhoj Airport EV Terminal",
            address = "Airport Road, Gandhi Nagar, Bhopal",
            latitude = 23.2875,
            longitude = 77.3378,
            distanceKm = 14.2,
            isOpen = true,
            availability = ChargerStatus.AVAILABLE,
            availableConnectors = 2,
            totalConnectors = 2,
            minimumTariff = Money(1500),
            rating = 4.4f,
            amenities = listOf("Airport Lounge", "24/7 Security")
        )
    )

    private fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return Math.round(r * c * 10.0) / 10.0
    }

    private var dynamicLocalStations: List<Station> = emptyList()

    private fun buildLocalStationsAround(lat: Double, lng: Double): List<Station> {
        val cosLat = Math.cos(Math.toRadians(lat)).coerceAtLeast(0.2)
        fun offsetLat(kmNorth: Double) = lat + (kmNorth / 111.0)
        fun offsetLng(kmEast: Double) = lng + (kmEast / (111.0 * cosLat))

        return listOf(
            Station(
                id = "st_local_1",
                name = "Spacetime Central Superhub",
                address = "Main Boulevard • Near Your Location",
                latitude = offsetLat(0.7),
                longitude = offsetLng(0.6),
                distanceKm = calculateDistanceKm(lat, lng, offsetLat(0.7), offsetLng(0.6)),
                isOpen = true,
                availability = ChargerStatus.AVAILABLE,
                availableConnectors = 4,
                totalConnectors = 6,
                minimumTariff = Money(1400),
                rating = 4.8f,
                amenities = listOf("Cafe", "WiFi", "24/7 Security")
            ),
            Station(
                id = "st_local_2",
                name = "Spacetime Metro DC Fast Plaza",
                address = "Commercial District • Sector Center",
                latitude = offsetLat(-1.1),
                longitude = offsetLng(-0.9),
                distanceKm = calculateDistanceKm(lat, lng, offsetLat(-1.1), offsetLng(-0.9)),
                isOpen = true,
                availability = ChargerStatus.AVAILABLE,
                availableConnectors = 3,
                totalConnectors = 4,
                minimumTariff = Money(1500),
                rating = 4.7f,
                amenities = listOf("Restroom", "Food Court", "Covered Parking")
            ),
            Station(
                id = "st_local_3",
                name = "GreenGrid Expressway Hub",
                address = "Bypass Corridor • Fast Charge Zone",
                latitude = offsetLat(-1.8),
                longitude = offsetLng(1.6),
                distanceKm = calculateDistanceKm(lat, lng, offsetLat(-1.8), offsetLng(1.6)),
                isOpen = true,
                availability = ChargerStatus.CHARGING,
                availableConnectors = 1,
                totalConnectors = 6,
                minimumTariff = Money(1350),
                rating = 4.6f,
                amenities = listOf("Restroom", "Waiting Lounge", "EV Spares")
            ),
            Station(
                id = "st_local_4",
                name = "Spacetime Tech Park Station",
                address = "Innovation Avenue • Gate 2",
                latitude = offsetLat(2.1),
                longitude = offsetLng(-1.5),
                distanceKm = calculateDistanceKm(lat, lng, offsetLat(2.1), offsetLng(-1.5)),
                isOpen = true,
                availability = ChargerStatus.AVAILABLE,
                availableConnectors = 5,
                totalConnectors = 8,
                minimumTariff = Money(1550),
                rating = 4.9f,
                amenities = listOf("Workspace", "Cafe", "Valet")
            ),
            Station(
                id = "st_local_5",
                name = "City Mall Quick Charger",
                address = "Basement P1 • East Entrance",
                latitude = offsetLat(0.3),
                longitude = offsetLng(-2.4),
                distanceKm = calculateDistanceKm(lat, lng, offsetLat(0.3), offsetLng(-2.4)),
                isOpen = true,
                availability = ChargerStatus.AVAILABLE,
                availableConnectors = 2,
                totalConnectors = 4,
                minimumTariff = Money(1450),
                rating = 4.5f,
                amenities = listOf("Shopping", "Restroom", "Multiplex")
            )
        )
    }

    override suspend fun getNearbyStations(lat: Double, lng: Double, radiusKm: Double): AppResult<List<Station>> {
        delay(250)
        val pilotWithDistances = stations.map { station ->
            station.copy(distanceKm = calculateDistanceKm(lat, lng, station.latitude, station.longitude))
        }
        val minPilotDist = pilotWithDistances.minOfOrNull { it.distanceKm } ?: Double.MAX_VALUE
        dynamicLocalStations = if (minPilotDist > 25.0) {
            buildLocalStationsAround(lat, lng)
        } else {
            emptyList()
        }
        val combined = (dynamicLocalStations + pilotWithDistances).sortedBy { it.distanceKm }
        return AppResult.Success(combined)
    }

    override suspend fun getStationDetails(stationId: String): AppResult<Station> {
        delay(200)
        val all = dynamicLocalStations + stations
        val s = all.find { it.id == stationId } ?: all.first()
        return AppResult.Success(s)
    }

    override suspend fun getStationChargers(stationId: String): AppResult<List<Charger>> {
        delay(300)
        return AppResult.Success(
            listOf(
                Charger(
                    chargerId = "CHG-01-DC",
                    displayName = "60kW Dual Gun DC Fast Charger",
                    ratedPowerKw = 60.0,
                    chargerType = "DC",
                    status = ChargerStatus.AVAILABLE,
                    connectors = listOf(
                        Connector(1, "CCS2", ChargerStatus.AVAILABLE),
                        Connector(2, "CCS2", ChargerStatus.AVAILABLE)
                    )
                ),
                Charger(
                    chargerId = "CHG-02-AC",
                    displayName = "22kW Type-2 AC Charger",
                    ratedPowerKw = 22.0,
                    chargerType = "AC",
                    status = ChargerStatus.AVAILABLE,
                    connectors = listOf(
                        Connector(1, "Type 2", ChargerStatus.AVAILABLE)
                    )
                )
            )
        )
    }

    override suspend fun searchStations(query: String): AppResult<List<Station>> {
        delay(200)
        val filtered = (dynamicLocalStations + stations).filter {
            it.name.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true)
        }
        return AppResult.Success(filtered)
    }
}

@Singleton
class FakeChargingRepository @Inject constructor() : ChargingRepository {

    private var activeSession: ActiveChargingSession? = null
    private var lastSummary: SessionSummary? = null
    private val sessionHistory = mutableListOf(
        SessionSummary(
            sessionId = "sess_hist_101",
            stationName = "Highway EV Superhub Jabalpur",
            chargerId = "CHG-01-DC",
            connectorId = 1,
            totalEnergyKwh = 28.4,
            durationFormatted = "38m 15s",
            startTime = "Yesterday, 06:12 PM",
            endTime = "Yesterday, 06:50 PM",
            tariffRate = Money(1400),
            sessionFee = Money(0),
            parkingFee = Money(0),
            taxAmount = Money(7156),
            discountAmount = Money(2000),
            finalAmount = Money(37760),
            paymentMethod = "Spacetime Wallet",
            paymentStatus = PaymentStatus.CAPTURED,
            invoiceUrl = "https://example.com/invoices/inv_101.pdf"
        ),
        SessionSummary(
            sessionId = "sess_hist_102",
            stationName = "Civic Centre Fast Charger",
            chargerId = "CHG-02-DC",
            connectorId = 2,
            totalEnergyKwh = 16.2,
            durationFormatted = "24m 05s",
            startTime = "06 Nov, 11:20 AM",
            endTime = "06 Nov, 11:44 AM",
            tariffRate = Money(1500),
            sessionFee = Money(0),
            parkingFee = Money(0),
            taxAmount = Money(4374),
            discountAmount = Money(0),
            finalAmount = Money(24300),
            paymentMethod = "UPI Autopay",
            paymentStatus = PaymentStatus.CAPTURED,
            invoiceUrl = "https://example.com/invoices/inv_102.pdf"
        ),
        SessionSummary(
            sessionId = "sess_hist_103",
            stationName = "DB City Mall EV Station Bhopal",
            chargerId = "CHG-04-DC",
            connectorId = 1,
            totalEnergyKwh = 34.0,
            durationFormatted = "45m 10s",
            startTime = "02 Nov, 03:40 PM",
            endTime = "02 Nov, 04:25 PM",
            tariffRate = Money(1600),
            sessionFee = Money(0),
            parkingFee = Money(0),
            taxAmount = Money(9792),
            discountAmount = Money(5000),
            finalAmount = Money(49400),
            paymentMethod = "Spacetime Wallet",
            paymentStatus = PaymentStatus.CAPTURED,
            invoiceUrl = "https://example.com/invoices/inv_103.pdf"
        )
    )

    override suspend fun resolveQr(qrPayload: String): AppResult<ChargingQuote> {
        delay(500)
        return AppResult.Success(
            ChargingQuote(
                quoteId = "quote_991",
                stationId = "st_001",
                chargerId = "CHG-01-DC",
                connectorId = 1,
                energyRatePerKwh = Money(1400),
                sessionFee = Money(0),
                estimatedTotal = Money(35000),
                walletAvailable = Money(75000)
            )
        )
    }

    override suspend fun getQuote(quoteId: String): AppResult<ChargingQuote> {
        delay(300)
        return AppResult.Success(
            ChargingQuote(
                quoteId = quoteId,
                stationId = "st_001",
                chargerId = "CHG-01-DC",
                connectorId = 1,
                energyRatePerKwh = Money(1400),
                sessionFee = Money(0),
                estimatedTotal = Money(35000),
                walletAvailable = Money(75000)
            )
        )
    }

    override suspend fun createQuote(stationId: String, chargerId: String, connectorId: Int): AppResult<ChargingQuote> {
        delay(400)
        return AppResult.Success(
            ChargingQuote(
                quoteId = "quote_${System.currentTimeMillis()}",
                stationId = stationId,
                chargerId = chargerId,
                connectorId = connectorId,
                energyRatePerKwh = Money(1400),
                sessionFee = Money(0),
                estimatedTotal = Money(35000),
                walletAvailable = Money(75000)
            )
        )
    }

    override suspend fun startSession(quoteId: String, paymentMode: String): AppResult<String> {
        delay(800)
        val sId = "sess_${System.currentTimeMillis()}"
        activeSession = ActiveChargingSession(
            sessionId = sId,
            status = SessionStatus.CHARGING,
            stationName = "Highway EV Superhub Jabalpur",
            chargerId = "CHG-01-DC",
            connectorId = 1,
            startedAt = "10:30 AM",
            energyWh = 22450,
            energyKwh = 22.45,
            powerKw = 52.6,
            estimatedAmount = Money(31430),
            durationSeconds = 1840
        )
        return AppResult.Success(sId)
    }

    override suspend fun getActiveSession(): AppResult<ActiveChargingSession?> {
        delay(200)
        return AppResult.Success(activeSession)
    }

    override suspend fun stopSession(sessionId: String): AppResult<SessionSummary> {
        delay(800)
        val summary = SessionSummary(
            sessionId = sessionId,
            stationName = activeSession?.stationName ?: "Highway EV Superhub Jabalpur",
            chargerId = activeSession?.chargerId ?: "CHG-01-DC",
            connectorId = activeSession?.connectorId ?: 1,
            totalEnergyKwh = activeSession?.energyKwh ?: 22.45,
            durationFormatted = "32m 40s",
            startTime = "10:30 AM",
            endTime = "11:02 AM",
            tariffRate = Money(1400),
            sessionFee = Money(0),
            parkingFee = Money(0),
            taxAmount = Money(5657),
            discountAmount = Money(0),
            finalAmount = Money(31430),
            paymentMethod = "Spacetime Wallet",
            paymentStatus = PaymentStatus.CAPTURED,
            invoiceUrl = "https://example.com/invoices/inv_123.pdf"
        )
        lastSummary = summary
        sessionHistory.add(0, summary)
        activeSession = null
        return AppResult.Success(summary)
    }

    override suspend fun getSessionSummary(sessionId: String): AppResult<SessionSummary> {
        delay(250)
        val found = sessionHistory.find { it.sessionId == sessionId } ?: lastSummary ?: sessionHistory.first()
        return AppResult.Success(found)
    }

    override suspend fun getSessionHistory(): AppResult<List<SessionSummary>> {
        delay(250)
        return AppResult.Success(sessionHistory.toList())
    }
}

@Singleton
class FakeWalletRepository @Inject constructor() : WalletRepository {

    private var availableMinor: Long = 75000
    private val transactions = mutableListOf(
        WalletTransaction("tx_01", "Top-up via UPI", Money(50000), "SUCCESS", reference = "UPI/9876543210@okhdfc", timestamp = "Today, 10:30 AM"),
        WalletTransaction("tx_02", "Charging Deduction", Money(-24500), "SUCCESS", relatedSessionId = "sess_001", reference = "CHG-01-DC / Jabalpur Hub", timestamp = "Yesterday, 04:15 PM"),
        WalletTransaction("tx_03", "Promotional Cashback", Money(2500), "SUCCESS", reference = "EV Welcome Credit", timestamp = "04 Nov 2026"),
        WalletTransaction("tx_04", "Charging Deduction", Money(-31430), "SUCCESS", relatedSessionId = "sess_002", reference = "CHG-02-AC / Civic Centre", timestamp = "02 Nov 2026")
    )

    override suspend fun getWallet(): AppResult<Wallet> {
        delay(200)
        return AppResult.Success(
            Wallet(
                walletId = "wal_101",
                availableBalance = Money(availableMinor),
                reservedBalance = Money(5000),
                promotionalBalance = Money(10000)
            )
        )
    }

    override suspend fun createTopUp(amountMinor: Long): AppResult<String> {
        delay(600)
        availableMinor += amountMinor
        val txId = "tx_${System.currentTimeMillis()}"
        transactions.add(0, WalletTransaction(txId, "Top-up via UPI", Money(amountMinor), "SUCCESS", reference = "Razorpay / UPI Intent", timestamp = "Just now"))
        return AppResult.Success(txId)
    }

    override suspend fun getTransactions(): AppResult<List<WalletTransaction>> {
        delay(300)
        return AppResult.Success(transactions.toList())
    }
}

@Singleton
class FakeSiteAdminRepository @Inject constructor(
    private val stationRepository: StationRepository
) : SiteAdminRepository {

    private val employees = mutableListOf(
        SiteEmployee("emp_01", "Vikram Patel", "+919876501234", "Site Manager", "Highway EV Superhub Jabalpur"),
        SiteEmployee("emp_02", "Amit Verma", "+919876505678", "Station Operator", "Highway EV Superhub Jabalpur"),
        SiteEmployee("emp_03", "Pooja Mehta", "+919876509012", "Accountant", "All Authorized Sites")
    )

    override suspend fun getDashboard(siteId: String?): AppResult<SiteAdminDashboard> {
        delay(400)
        return AppResult.Success(
            SiteAdminDashboard(
                grossRevenue = Money(845000),
                energyDeliveredWh = 182000,
                sessionCount = 24,
                uniqueCustomerCount = 19,
                availableConnectors = 5,
                occupiedConnectors = 2,
                offlineConnectors = 1,
                faultedConnectors = 0
            )
        )
    }

    override suspend fun getStations(): AppResult<List<Station>> = stationRepository.getNearbyStations(23.18, 79.98)

    override suspend fun getRevenueSummary(): AppResult<SiteAdminRevenue> {
        delay(300)
        return AppResult.Success(
            SiteAdminRevenue(
                grossChargingValue = Money(845000),
                platformCommission = Money(84500),
                gatewayFees = Money(16900),
                taxAmount = Money(152100),
                netSettlement = Money(591500),
                paidSettlements = Money(420000),
                pendingSettlements = Money(171500)
            )
        )
    }

    override suspend fun getSettlements(): AppResult<List<SettlementRecord>> {
        delay(300)
        return AppResult.Success(
            listOf(
                SettlementRecord("SET-2026-11-A", "01 Nov - 07 Nov 2026", Money(420000), Money(105000), Money(315000), "PAID", "08 Nov 2026", "08 Nov 2026"),
                SettlementRecord("SET-2026-11-B", "08 Nov - 14 Nov 2026", Money(245000), Money(61250), Money(183750), "PROCESSING", "15 Nov 2026"),
                SettlementRecord("SET-2026-10-D", "25 Oct - 31 Oct 2026", Money(390000), Money(97500), Money(292500), "PAID", "01 Nov 2026", "01 Nov 2026")
            )
        )
    }

    override suspend fun getLiveSessions(): AppResult<List<SiteLiveSession>> {
        delay(300)
        return AppResult.Success(
            listOf(
                SiteLiveSession(
                    sessionId = "sess_live_101",
                    stationName = "Highway EV Superhub Jabalpur",
                    chargerId = "CHG-01-DC",
                    connectorId = 1,
                    customerMaskedMobile = "+91 98765****10",
                    energyKwh = 28.4,
                    powerKw = 54.2,
                    estimatedAmount = Money(39760),
                    startedAt = "10:15 AM"
                ),
                SiteLiveSession(
                    sessionId = "sess_live_102",
                    stationName = "Civic Centre Fast Charger",
                    chargerId = "CHG-02-DC",
                    connectorId = 2,
                    customerMaskedMobile = "+91 98231****44",
                    energyKwh = 14.8,
                    powerKw = 46.0,
                    estimatedAmount = Money(22200),
                    startedAt = "10:42 AM"
                )
            )
        )
    }

    override suspend fun getEmployees(): AppResult<List<SiteEmployee>> {
        delay(300)
        return AppResult.Success(employees.toList())
    }

    override suspend fun inviteEmployee(name: String, mobile: String, role: String, siteId: String): AppResult<SiteEmployee> {
        delay(400)
        val emp = SiteEmployee("emp_${System.currentTimeMillis()}", name, mobile, role, siteId)
        employees.add(emp)
        return AppResult.Success(emp)
    }
}

@Singleton
class FakeServiceProviderRepository @Inject constructor() : ServiceProviderRepository {

    private val installations = mutableListOf(
        InstallationProject(
            id = "INST-2026-081",
            customerName = "Highway Retails Pvt Ltd",
            siteName = "Jabalpur Highway Plaza Hub",
            address = "NH-34, Bypass Km 12, Jabalpur",
            chargerCount = 2,
            scheduledDate = "22 Nov 2026",
            status = "INSTALLATION_IN_PROGRESS",
            technicianName = "Suresh Raina",
            electricalSurveyDone = true,
            commissioningDone = false
        ),
        InstallationProject(
            id = "INST-2026-082",
            customerName = "City Mall Group",
            siteName = "Civic Centre EV Hub",
            address = "Marhatal, Jabalpur",
            chargerCount = 4,
            scheduledDate = "28 Nov 2026",
            status = "SURVEY_PENDING",
            technicianName = null,
            electricalSurveyDone = false,
            commissioningDone = false
        ),
        InstallationProject(
            id = "INST-2026-079",
            customerName = "Bhopal Metro Transit",
            siteName = "MP Nagar Metro Hub",
            address = "Hoshangabad Road, Bhopal",
            chargerCount = 6,
            scheduledDate = "10 Nov 2026",
            status = "COMMISSIONING_PENDING",
            technicianName = "Deepak Joshi",
            electricalSurveyDone = true,
            commissioningDone = true
        )
    )

    private val equipment = mutableListOf(
        EquipmentItem("EQ-01", "EVCHG-60-2026-001", "Delta Electronics", "DC60 Dual Fast", 60.0, "DC", listOf("CCS2"), "Highway Retails", "Jabalpur Highway Hub", "08 Nov 2028", "INSTALLED"),
        EquipmentItem("EQ-02", "EVCHG-60-2026-002", "Delta Electronics", "DC60 Dual Fast", 60.0, "DC", listOf("CCS2"), "City Mall Group", "Civic Centre", "15 Dec 2028", "DISPATCHED"),
        EquipmentItem("EQ-03", "EVCHG-22-2026-008", "ABB", "Terra AC Wallbox", 22.0, "AC", listOf("Type 2"), "Spacetime Inventory", "Central Warehouse Jabalpur", "20 Oct 2027", "IN_STOCK"),
        EquipmentItem("EQ-04", "EVCHG-120-2026-014", "Schneider Electric", "EVlink Ultra 120", 120.0, "DC", listOf("CCS2", "CHAdeMO"), "Highway Retails", "Jabalpur Highway Hub", "12 Sep 2028", "UNDER_MAINTENANCE")
    )

    private val tickets = mutableListOf(
        ServiceTicket(
            id = "TKT-891",
            customerName = "Highway Retails",
            stationName = "Jabalpur Highway Hub",
            chargerId = "CHG-01-DC",
            problemDescription = "Gun #2 communication module timeout during OCPP handshake.",
            priority = "HIGH",
            slaDueFormatted = "Today, 04:00 PM (4h left)",
            assignedTechnician = "Deepak Joshi",
            status = "IN_PROGRESS"
        ),
        ServiceTicket(
            id = "TKT-892",
            customerName = "City Mall Group",
            stationName = "Civic Centre Hub",
            chargerId = "CHG-02-AC",
            problemDescription = "RFID tap card reader unresponsive.",
            priority = "MEDIUM",
            slaDueFormatted = "Tomorrow, 12:00 PM",
            assignedTechnician = null,
            status = "OPEN"
        )
    )

    private val technicians = mutableListOf(
        TechnicianProfile("TECH-01", "Deepak Joshi", "+919876541100", "Jabalpur Division", listOf("DC Fast Chargers", "OCPP Telemetry", "Earthing"), "ON_DUTY", 1),
        TechnicianProfile("TECH-02", "Suresh Raina", "+919876542200", "Jabalpur Division", listOf("High Voltage Civil", "Distribution Panels"), "AVAILABLE", 0),
        TechnicianProfile("TECH-03", "Anil Kumble", "+919876543300", "Bhopal Division", listOf("ABB Chargers", "Firmware Diagnostics"), "AVAILABLE", 0)
    )

    override suspend fun getDashboard(): AppResult<ProviderDashboard> {
        delay(300)
        return AppResult.Success(
            ProviderDashboard(
                totalCustomers = 18,
                totalInstallations = 42,
                totalChargersInstalled = 76,
                pendingInstallations = 5,
                activeInstallations = 3,
                openServiceTickets = 2,
                warrantyExpiring = 4,
                amcExpiring = 2
            )
        )
    }

    override suspend fun getInstallations(): AppResult<List<InstallationProject>> {
        delay(300)
        return AppResult.Success(installations.toList())
    }

    override suspend fun createInstallation(project: InstallationProject): AppResult<InstallationProject> {
        delay(500)
        val newProj = project.copy(id = "INST-2026-0${installations.size + 80}")
        installations.add(0, newProj)
        return AppResult.Success(newProj)
    }

    override suspend fun updateInstallationStatus(id: String, status: String): AppResult<Unit> {
        delay(300)
        val idx = installations.indexOfFirst { it.id == id }
        if (idx != -1) {
            installations[idx] = installations[idx].copy(status = status)
        }
        return AppResult.Success(Unit)
    }

    override suspend fun getEquipment(): AppResult<List<EquipmentItem>> {
        delay(300)
        return AppResult.Success(equipment.toList())
    }

    override suspend fun registerEquipment(item: EquipmentItem): AppResult<EquipmentItem> {
        delay(400)
        equipment.add(0, item)
        return AppResult.Success(item)
    }

    override suspend fun getServiceTickets(): AppResult<List<ServiceTicket>> {
        delay(300)
        return AppResult.Success(tickets.toList())
    }

    override suspend fun assignTicket(ticketId: String, technicianName: String): AppResult<Unit> {
        delay(300)
        val idx = tickets.indexOfFirst { it.id == ticketId }
        if (idx != -1) {
            tickets[idx] = tickets[idx].copy(assignedTechnician = technicianName, status = "ASSIGNED")
        }
        return AppResult.Success(Unit)
    }

    override suspend fun resolveTicket(ticketId: String, resolutionNotes: String): AppResult<Unit> {
        delay(400)
        val idx = tickets.indexOfFirst { it.id == ticketId }
        if (idx != -1) {
            tickets[idx] = tickets[idx].copy(status = "RESOLVED")
        }
        return AppResult.Success(Unit)
    }

    override suspend fun getTechnicians(): AppResult<List<TechnicianProfile>> {
        delay(200)
        return AppResult.Success(technicians.toList())
    }
}

@Singleton
class FakeVehicleRepository @Inject constructor() : VehicleRepository {

    private val vehicles = mutableListOf(
        Vehicle(
            id = "veh_001",
            nickname = "Nexon EV Prime",
            category = "FOUR_WHEELER",
            manufacturer = "Tata Motors",
            model = "Nexon EV Max",
            registrationNumber = "MP20AB1234",
            batteryCapacityKwh = 40.5,
            connectorTypes = listOf("CCS2", "Type 2"),
            maxAcPowerKw = 7.2,
            maxDcPowerKw = 50.0,
            isDefault = true
        ),
        Vehicle(
            id = "veh_002",
            nickname = "City Cruiser",
            category = "FOUR_WHEELER",
            manufacturer = "MG",
            model = "ZS EV",
            registrationNumber = "MP20CD5678",
            batteryCapacityKwh = 50.3,
            connectorTypes = listOf("CCS2", "Type 2"),
            maxAcPowerKw = 7.4,
            maxDcPowerKw = 60.0,
            isDefault = false
        )
    )

    override suspend fun getVehicles(): AppResult<List<Vehicle>> {
        delay(300)
        return AppResult.Success(vehicles.toList())
    }

    override suspend fun addVehicle(vehicle: Vehicle): AppResult<Vehicle> {
        delay(500)
        val newVeh = vehicle.copy(id = "veh_${System.currentTimeMillis()}")
        vehicles.add(newVeh)
        return AppResult.Success(newVeh)
    }

    override suspend fun deleteVehicle(vehicleId: String): AppResult<Unit> {
        delay(300)
        vehicles.removeAll { it.id == vehicleId }
        return AppResult.Success(Unit)
    }

    override suspend fun setDefaultVehicle(vehicleId: String): AppResult<Unit> {
        delay(200)
        for (i in vehicles.indices) {
            vehicles[i] = vehicles[i].copy(isDefault = vehicles[i].id == vehicleId)
        }
        return AppResult.Success(Unit)
    }
}
