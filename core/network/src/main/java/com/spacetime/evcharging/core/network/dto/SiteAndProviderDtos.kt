package com.spacetime.evcharging.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SiteAdminDashboardDto(
    @SerialName("gross_revenue_minor") val grossRevenueMinor: Long,
    @SerialName("energy_delivered_wh") val energyDeliveredWh: Long,
    @SerialName("session_count") val sessionCount: Int,
    @SerialName("unique_customer_count") val uniqueCustomerCount: Int,
    @SerialName("available_connectors") val availableConnectors: Int,
    @SerialName("occupied_connectors") val occupiedConnectors: Int,
    @SerialName("offline_connectors") val offlineConnectors: Int,
    @SerialName("faulted_connectors") val faultedConnectors: Int,
    @SerialName("currency") val currency: String = "INR"
)

@Serializable
data class ProviderDashboardDto(
    @SerialName("total_customers") val totalCustomers: Int,
    @SerialName("total_installations") val totalInstallations: Int,
    @SerialName("total_chargers_installed") val totalChargersInstalled: Int,
    @SerialName("pending_installations") val pendingInstallations: Int,
    @SerialName("active_installations") val activeInstallations: Int,
    @SerialName("open_service_tickets") val openServiceTickets: Int,
    @SerialName("warranty_expiring") val warrantyExpiring: Int,
    @SerialName("amc_expiring") val amcExpiring: Int
)
