package com.spacetime.evcharging.core.network.api

import com.spacetime.evcharging.core.network.dto.*
import com.spacetime.evcharging.core.network.model.ApiResponse
import retrofit2.http.*

interface AuthApiService {
    @POST("auth/otp/send")
    suspend fun sendOtp(@Body request: SendOtpRequest): ApiResponse<SendOtpResponse>

    @POST("auth/otp/verify")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): ApiResponse<VerifyOtpResponse>

    @POST("auth/token/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): ApiResponse<RefreshTokenResponse>

    @POST("auth/logout")
    suspend fun logout(@Body request: LogoutRequest): ApiResponse<Unit>

    @GET("me")
    suspend fun getCurrentUser(): ApiResponse<UserDto>

    @POST("me/active-role")
    suspend fun switchRole(@Body request: SwitchRoleRequest): ApiResponse<UserDto>

    @PUT("me/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): ApiResponse<UserDto>
}

interface StationApiService {
    @GET("stations/nearby")
    suspend fun getNearbyStations(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius_km") radiusKm: Double = 25.0,
        @Query("availability") availability: String? = null,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): ApiResponse<List<StationDto>>

    @GET("stations/{station_id}")
    suspend fun getStationDetails(@Path("station_id") stationId: String): ApiResponse<StationDto>

    @GET("stations/{station_id}/chargers")
    suspend fun getStationChargers(@Path("station_id") stationId: String): ApiResponse<List<ChargerDto>>

    @GET("stations/search")
    suspend fun searchStations(@Query("q") query: String): ApiResponse<List<StationDto>>
}

interface ChargingApiService {
    @POST("charging/qr/resolve")
    suspend fun resolveQr(@Body request: QrResolveRequest): ApiResponse<QrResolveResponse>

    @POST("charging/quote")
    suspend fun createQuote(@Body request: ChargingQuoteRequest): ApiResponse<ChargingQuoteResponse>

    @POST("charging/sessions/start")
    suspend fun startSession(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body request: StartSessionRequest
    ): ApiResponse<StartSessionResponse>

    @GET("charging/sessions/active")
    suspend fun getActiveSession(): ApiResponse<ActiveSessionDto>

    @POST("charging/sessions/{session_id}/stop")
    suspend fun stopSession(
        @Path("session_id") sessionId: String,
        @Body request: StopSessionRequest
    ): ApiResponse<StopSessionResponse>
}

interface WalletApiService {
    @GET("wallet")
    suspend fun getWallet(): ApiResponse<WalletDto>

    @POST("wallet/topups")
    suspend fun createTopUp(@Body request: TopUpRequest): ApiResponse<TopUpResponse>

    @GET("wallet/transactions")
    suspend fun getTransactions(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): ApiResponse<List<WalletTransactionDto>>
}

interface SiteAdminApiService {
    @GET("site-admin/dashboard")
    suspend fun getDashboard(
        @Query("site_id") siteId: String? = null,
        @Query("from_date") fromDate: String? = null,
        @Query("to_date") toDate: String? = null
    ): ApiResponse<SiteAdminDashboardDto>

    @GET("site-admin/stations")
    suspend fun getStations(): ApiResponse<List<StationDto>>
}

interface ProviderApiService {
    @GET("service-provider/dashboard")
    suspend fun getDashboard(): ApiResponse<ProviderDashboardDto>
}
