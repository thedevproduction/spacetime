package com.spacetime.evcharging.core.network.di;

import com.spacetime.evcharging.core.network.api.ChargingApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class NetworkModule_ProvideChargingApiServiceFactory implements Factory<ChargingApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideChargingApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public ChargingApiService get() {
    return provideChargingApiService(retrofitProvider.get());
  }

  public static NetworkModule_ProvideChargingApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideChargingApiServiceFactory(retrofitProvider);
  }

  public static ChargingApiService provideChargingApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideChargingApiService(retrofit));
  }
}
