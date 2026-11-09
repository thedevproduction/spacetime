package com.spacetime.evcharging.core.security.di

import com.spacetime.evcharging.core.security.TokenManager
import com.spacetime.evcharging.core.security.TokenManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityModule {

    @Binds
    @Singleton
    abstract fun bindTokenManager(impl: TokenManagerImpl): TokenManager
}
