package com.spacetime.evcharging.core.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface TokenManager {
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun saveAccessToken(token: String)
    suspend fun getAccessToken(): String?
    suspend fun saveRefreshToken(token: String)
    suspend fun getRefreshToken(): String?
    suspend fun clearTokens()
    suspend fun isLoggedIn(): Boolean
}

@Singleton
class TokenManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenManager {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            "spacetime_secure_tokens",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) = withContext(Dispatchers.IO) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .apply()
    }

    override suspend fun saveAccessToken(token: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    override suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    override suspend fun saveRefreshToken(token: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString(KEY_REFRESH_TOKEN, token).apply()
    }

    override suspend fun getRefreshToken(): String? = withContext(Dispatchers.IO) {
        prefs.getString(KEY_REFRESH_TOKEN, null)
    }

    override suspend fun clearTokens() = withContext(Dispatchers.IO) {
        prefs.edit().clear().apply()
    }

    override suspend fun isLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        !prefs.getString(KEY_ACCESS_TOKEN, null).isNullOrBlank()
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
