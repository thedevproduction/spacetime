package com.spacetime.evcharging.core.network.dto

import com.spacetime.evcharging.core.network.model.MoneyDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StationDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("address") val address: String,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("distance_km") val distanceKm: Double? = null,
    @SerialName("is_open") val isOpen: Boolean = true,
    @SerialName("availability") val availability: String = "AVAILABLE",
    @SerialName("available_connectors") val availableConnectors: Int = 0,
    @SerialName("total_connectors") val totalConnectors: Int = 0,
    @SerialName("minimum_tariff") val minimumTariff: MoneyDto? = null,
    @SerialName("rating") val rating: Float = 4.5f,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("amenities") val amenities: List<String> = emptyList(),
    @SerialName("operating_hours") val operatingHours: String? = "24 Hours",
    @SerialName("last_status_update") val lastStatusUpdate: String? = null
)

@Serializable
data class ChargerDto(
    @SerialName("charger_id") val chargerId: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("rated_power_kw") val ratedPowerKw: Double,
    @SerialName("charger_type") val chargerType: String,
    @SerialName("status") val status: String,
    @SerialName("connectors") val connectors: List<ConnectorDto> = emptyList()
)

@Serializable
data class ConnectorDto(
    @SerialName("connector_id") val connectorId: Int,
    @SerialName("connector_type") val connectorType: String,
    @SerialName("status") val status: String,
    @SerialName("tariff_id") val tariffId: String? = null
)
