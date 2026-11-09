package com.spacetime.evcharging.feature.charging;

import androidx.lifecycle.SavedStateHandle;
import com.spacetime.evcharging.data.repository.ChargingRepository;
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
public final class ChargingViewModel_Factory implements Factory<ChargingViewModel> {
  private final Provider<ChargingRepository> chargingRepositoryProvider;

  private final Provider<WalletRepository> walletRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public ChargingViewModel_Factory(Provider<ChargingRepository> chargingRepositoryProvider,
      Provider<WalletRepository> walletRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.chargingRepositoryProvider = chargingRepositoryProvider;
    this.walletRepositoryProvider = walletRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public ChargingViewModel get() {
    return newInstance(chargingRepositoryProvider.get(), walletRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static ChargingViewModel_Factory create(
      Provider<ChargingRepository> chargingRepositoryProvider,
      Provider<WalletRepository> walletRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new ChargingViewModel_Factory(chargingRepositoryProvider, walletRepositoryProvider, savedStateHandleProvider);
  }

  public static ChargingViewModel newInstance(ChargingRepository chargingRepository,
      WalletRepository walletRepository, SavedStateHandle savedStateHandle) {
    return new ChargingViewModel(chargingRepository, walletRepository, savedStateHandle);
  }
}
