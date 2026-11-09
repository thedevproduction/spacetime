package com.spacetime.evcharging.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QrResolveRequest(
    @SerialName("qr_payload") val qrPayload: String
)

@Serializable
data class QrResolveResponse(
    @SerialName("station") val station: QrStationDto,
    @SerialName("charger") val charger: QrChargerDto,
    @SerialName("connector") val connector: QrConnectorDto,
    @SerialName("is_eligible_to_start") val isEligibleToStart: Boolean,
    @SerialName("ineligibility_reason") val ineligibilityReason: String? = null
)

@Serializable
data class QrStationDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String
)

@Serializable
data class QrChargerDto(
    @SerialName("id") val id: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("rated_power_kw") val ratedPowerKw: Double
)

@Serializable
data class QrConnectorDto(
    @SerialName("id") val id: Int,
    @SerialName("type") val type: String,
    @SerialName("status") val status: String
)

@Serializable
data class ChargingQuoteRequest(
    @SerialName("station_id") val stationId: String,
    @SerialName("charger_id") val chargerId: String,
    @SerialName("connector_id") val connectorId: Int,
    @SerialName("vehicle_id") val vehicleId: String? = null,
    @SerialName("payment_method") val paymentMethod: String = "WALLET",
    @SerialName("requested_amount_minor") val requestedAmountMinor: Long? = null
)

@Serializable
data class ChargingQuoteResponse(
    @SerialName("quote_id") val quoteId: String,
    @SerialName("expires_at") val expiresAt: String,
    @SerialName("station_id") val stationId: String,
    @SerialName("charger_id") val chargerId: String,
    @SerialName("connector_id") val connectorId: Int,
    @SerialName("currency") val currency: String = "INR",
    @SerialName("pricing") val pricing: QuotePricingDto,
    @SerialName("estimated_total_minor") val estimatedTotalMinor: Long,
    @SerialName("wallet_available_minor") val walletAvailableMinor: Long,
    @SerialName("payment_method") val paymentMethod: String,
    @SerialName("requires_payment_authorization") val requiresPaymentAuthorization: Boolean
)

@Serializable
data class QuotePricingDto(
    @SerialName("energy_rate_minor_per_kwh") val energyRateMinorPerKwh: Long,
    @SerialName("session_fee_minor") val sessionFeeMinor: Long = 0,
    @SerialName("parking_fee_minor") val parkingFeeMinor: Long = 0,
    @SerialName("idle_fee_minor_per_minute") val idleFeeMinorPerMinute: Long = 0,
    @SerialName("tax_minor_estimate") val taxMinorEstimate: Long = 0
)

@Serializable
data class StartSessionRequest(
    @SerialName("quote_id") val quoteId: String,
    @SerialName("payment_reference") val paymentReference: String? = null,
    @SerialName("payment_mode") val paymentMode: String = "WALLET"
)

@Serializable
data class StartSessionResponse(
    @SerialName("session_id") val sessionId: String,
    @SerialName("status") val status: String,
    @SerialName("charger_id") val chargerId: String,
    @SerialName("connector_id") val connectorId: Int,
    @SerialName("message") val message: String
)

@Serializable
data class ActiveSessionDto(
    @SerialName("session_id") val sessionId: String,
    @SerialName("status") val status: String,
    @SerialName("station_name") val stationName: String,
    @SerialName("charger_id") val chargerId: String,
    @SerialName("connector_id") val connectorId: Int,
    @SerialName("started_at") val startedAt: String,
    @SerialName("energy_wh") val energyWh: Long,
    @SerialName("energy_kwh") val energyKwh: Double,
    @SerialName("power_w") val powerW: Long,
    @SerialName("power_kw") val powerKw: Double,
    @SerialName("estimated_amount_minor") val estimatedAmountMinor: Long,
    @SerialName("currency") val currency: String = "INR",
    @SerialName("last_meter_update") val lastMeterUpdate: String? = null
)

@Serializable
data class StopSessionRequest(
    @SerialName("reason") val reason: String = "CUSTOMER_REQUEST"
)

@Serializable
data class StopSessionResponse(
    @SerialName("session_id") val sessionId: String,
    @SerialName("status") val status: String,
    @SerialName("message") val message: String
)
