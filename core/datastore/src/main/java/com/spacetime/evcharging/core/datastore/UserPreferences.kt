package com.spacetime.evcharging.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

interface UserPreferences {
    val activeRole: Flow<String?>
    val preferredLanguage: Flow<String>
    val onboardingCompleted: Flow<Boolean>
    val selectedCity: Flow<String?>
    val deviceId: Flow<String>

    suspend fun setActiveRole(role: String)
    suspend fun setPreferredLanguage(language: String)
    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun setSelectedCity(city: String)
    suspend fun setDeviceId(deviceId: String)
    suspend fun clear()
}

@Singleton
class UserPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserPreferences {

    private val dataStore = context.userDataStore

    override val activeRole: Flow<String?> = dataStore.data.map { it[KEY_ACTIVE_ROLE] }
    override val preferredLanguage: Flow<String> = dataStore.data.map { it[KEY_PREFERRED_LANGUAGE] ?: "en-IN" }
    override val onboardingCompleted: Flow<Boolean> = dataStore.data.map { it[KEY_ONBOARDING_COMPLETED] ?: false }
    override val selectedCity: Flow<String?> = dataStore.data.map { it[KEY_SELECTED_CITY] }
    override val deviceId: Flow<String> = dataStore.data.map { it[KEY_DEVICE_ID] ?: "device_default" }

    override suspend fun setActiveRole(role: String) {
        dataStore.edit { it[KEY_ACTIVE_ROLE] = role }
    }

    override suspend fun setPreferredLanguage(language: String) {
        dataStore.edit { it[KEY_PREFERRED_LANGUAGE] = language }
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { it[KEY_ONBOARDING_COMPLETED] = completed }
    }

    override suspend fun setSelectedCity(city: String) {
        dataStore.edit { it[KEY_SELECTED_CITY] = city }
    }

    override suspend fun setDeviceId(deviceId: String) {
        dataStore.edit { it[KEY_DEVICE_ID] = deviceId }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    companion object {
        private val KEY_ACTIVE_ROLE = stringPreferencesKey("active_role")
        private val KEY_PREFERRED_LANGUAGE = stringPreferencesKey("preferred_language")
        private val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        private val KEY_SELECTED_CITY = stringPreferencesKey("selected_city")
        private val KEY_DEVICE_ID = stringPreferencesKey("device_id")
    }
}
