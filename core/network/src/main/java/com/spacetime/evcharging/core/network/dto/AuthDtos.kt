package com.spacetime.evcharging.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendOtpRequest(
    @SerialName("country_code") val countryCode: String = "+91",
    @SerialName("mobile_number") val mobileNumber: String,
    @SerialName("purpose") val purpose: String = "LOGIN"
)

@Serializable
data class SendOtpResponse(
    @SerialName("challenge_id") val challengeId: String,
    @SerialName("masked_mobile_number") val maskedMobileNumber: String,
    @SerialName("expires_in_seconds") val expiresInSeconds: Int,
    @SerialName("resend_after_seconds") val resendAfterSeconds: Int
)

@Serializable
data class VerifyOtpRequest(
    @SerialName("challenge_id") val challengeId: String,
    @SerialName("otp") val otp: String,
    @SerialName("device") val device: DeviceInfoDto
)

@Serializable
data class DeviceInfoDto(
    @SerialName("device_id") val deviceId: String,
    @SerialName("platform") val platform: String = "ANDROID",
    @SerialName("app_version") val appVersion: String = "1.0.0"
)

@Serializable
data class VerifyOtpResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("token_type") val tokenType: String = "Bearer",
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("is_new_user") val isNewUser: Boolean,
    @SerialName("user") val user: UserDto
)

@Serializable
data class UserDto(
    @SerialName("id") val id: String,
    @SerialName("full_name") val fullName: String?,
    @SerialName("mobile_number") val mobileNumber: String,
    @SerialName("email") val email: String? = null,
    @SerialName("profile_photo_url") val profilePhotoUrl: String? = null,
    @SerialName("status") val status: String,
    @SerialName("roles") val roles: List<UserRoleDto>,
    @SerialName("default_role") val defaultRole: String,
    @SerialName("preferred_language") val preferredLanguage: String? = "en-IN"
)

@Serializable
data class UserRoleDto(
    @SerialName("role") val role: String,
    @SerialName("scope_type") val scopeType: String? = null,
    @SerialName("scope_id") val scopeId: String? = null
)

@Serializable
data class RefreshTokenRequest(
    @SerialName("refresh_token") val refreshToken: String
)

@Serializable
data class RefreshTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("expires_in") val expiresIn: Int
)

@Serializable
data class LogoutRequest(
    @SerialName("device_id") val deviceId: String,
    @SerialName("logout_all_devices") val logoutAllDevices: Boolean = false
)

@Serializable
data class UpdateProfileRequest(
    @SerialName("full_name") val fullName: String,
    @SerialName("email") val email: String? = null,
    @SerialName("city") val city: String? = null,
    @SerialName("preferred_language") val preferredLanguage: String = "en-IN"
)

@Serializable
data class SwitchRoleRequest(
    @SerialName("role") val role: String,
    @SerialName("scope_id") val scopeId: String? = null
)
