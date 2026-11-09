package com.spacetime.evcharging.feature.customer.home;

import com.spacetime.evcharging.data.repository.AuthRepository;
import com.spacetime.evcharging.data.repository.ChargingRepository;
import com.spacetime.evcharging.data.repository.StationRepository;
import com.spacetime.evcharging.data.repository.WalletRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class CustomerHomeViewModel_Factory implements Factory<CustomerHomeViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<WalletRepository> walletRepositoryProvider;

  private final Provider<StationRepository> stationRepositoryProvider;

  private final Provider<ChargingRepository> chargingRepositoryProvider;

  public CustomerHomeViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<WalletRepository> walletRepositoryProvider,
      Provider<StationRepository> stationRepositoryProvider,
      Provider<ChargingRepository> chargingRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.walletRepositoryProvider = walletRepositoryProvider;
    this.stationRepositoryProvider = stationRepositoryProvider;
    this.chargingRepositoryProvider = chargingRepositoryProvider;
  }

  @Override
  public CustomerHomeViewModel get() {
    return newInstance(authRepositoryProvider.get(), walletRepositoryProvider.get(), stationRepositoryProvider.get(), chargingRepositoryProvider.get());
  }

  public static CustomerHomeViewModel_Factory create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<WalletRepository> walletRepositoryProvider,
      Provider<StationRepository> stationRepositoryProvider,
      Provider<ChargingRepository> chargingRepositoryProvider) {
    return new CustomerHomeViewModel_Factory(authRepositoryProvider, walletRepositoryProvider, stationRepositoryProvider, chargingRepositoryProvider);
  }

  public static CustomerHomeViewModel newInstance(AuthRepository authRepository,
      WalletRepository walletRepository, StationRepository stationRepository,
      ChargingRepository chargingRepository) {
    return new CustomerHomeViewModel(authRepository, walletRepository, stationRepository, chargingRepository);
  }
}
