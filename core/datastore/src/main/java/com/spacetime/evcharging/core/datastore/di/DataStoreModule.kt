package com.spacetime.evcharging.core.datastore.di

import com.spacetime.evcharging.core.datastore.UserPreferences
import com.spacetime.evcharging.core.datastore.UserPreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    @Binds
    @Singleton
    abstract fun bindUserPreferences(impl: UserPreferencesImpl): UserPreferences
}
