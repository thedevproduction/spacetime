package com.spacetime.evcharging.core.network.di;

import com.spacetime.evcharging.core.network.api.WalletApiService;
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
public final class NetworkModule_ProvideWalletApiServiceFactory implements Factory<WalletApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideWalletApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public WalletApiService get() {
    return provideWalletApiService(retrofitProvider.get());
  }

  public static NetworkModule_ProvideWalletApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideWalletApiServiceFactory(retrofitProvider);
  }

  public static WalletApiService provideWalletApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideWalletApiService(retrofit));
  }
}
