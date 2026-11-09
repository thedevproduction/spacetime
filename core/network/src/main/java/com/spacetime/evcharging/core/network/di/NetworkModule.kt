package com.spacetime.evcharging.core.network.di

import com.spacetime.evcharging.core.common.Constants
import com.spacetime.evcharging.core.network.api.*
import com.spacetime.evcharging.core.network.interceptor.AuthInterceptor
import com.spacetime.evcharging.core.network.interceptor.RequestIdInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = false
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        requestIdInterceptor: RequestIdInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(requestIdInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_DEV)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideStationApiService(retrofit: Retrofit): StationApiService =
        retrofit.create(StationApiService::class.java)

    @Provides
    @Singleton
    fun provideChargingApiService(retrofit: Retrofit): ChargingApiService =
        retrofit.create(ChargingApiService::class.java)

    @Provides
    @Singleton
    fun provideWalletApiService(retrofit: Retrofit): WalletApiService =
        retrofit.create(WalletApiService::class.java)

    @Provides
    @Singleton
    fun provideSiteAdminApiService(retrofit: Retrofit): SiteAdminApiService =
        retrofit.create(SiteAdminApiService::class.java)

    @Provides
    @Singleton
    fun provideProviderApiService(retrofit: Retrofit): ProviderApiService =
        retrofit.create(ProviderApiService::class.java)
}
