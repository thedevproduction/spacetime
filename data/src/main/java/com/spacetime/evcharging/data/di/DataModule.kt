package com.spacetime.evcharging.data.di

import com.spacetime.evcharging.data.repository.*
import com.spacetime.evcharging.data.repository.fake.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(fake: FakeAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindStationRepository(fake: FakeStationRepository): StationRepository

    @Binds
    @Singleton
    abstract fun bindChargingRepository(fake: FakeChargingRepository): ChargingRepository

    @Binds
    @Singleton
    abstract fun bindWalletRepository(fake: FakeWalletRepository): WalletRepository

    @Binds
    @Singleton
    abstract fun bindSiteAdminRepository(fake: FakeSiteAdminRepository): SiteAdminRepository

    @Binds
    @Singleton
    abstract fun bindServiceProviderRepository(fake: FakeServiceProviderRepository): ServiceProviderRepository

    @Binds
    @Singleton
    abstract fun bindVehicleRepository(fake: FakeVehicleRepository): VehicleRepository
}
