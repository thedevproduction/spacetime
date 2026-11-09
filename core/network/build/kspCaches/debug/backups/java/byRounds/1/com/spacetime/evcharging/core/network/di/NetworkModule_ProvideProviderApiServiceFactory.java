package com.spacetime.evcharging.core.network.di;

import com.spacetime.evcharging.core.network.api.ProviderApiService;
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
public final class NetworkModule_ProvideProviderApiServiceFactory implements Factory<ProviderApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideProviderApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public ProviderApiService get() {
    return provideProviderApiService(retrofitProvider.get());
  }

  public static NetworkModule_ProvideProviderApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideProviderApiServiceFactory(retrofitProvider);
  }

  public static ProviderApiService provideProviderApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideProviderApiService(retrofit));
  }
}
