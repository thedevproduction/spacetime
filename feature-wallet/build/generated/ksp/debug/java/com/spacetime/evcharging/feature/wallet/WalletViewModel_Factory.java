package com.spacetime.evcharging.feature.wallet;

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
public final class WalletViewModel_Factory implements Factory<WalletViewModel> {
  private final Provider<WalletRepository> walletRepositoryProvider;

  public WalletViewModel_Factory(Provider<WalletRepository> walletRepositoryProvider) {
    this.walletRepositoryProvider = walletRepositoryProvider;
  }

  @Override
  public WalletViewModel get() {
    return newInstance(walletRepositoryProvider.get());
  }

  public static WalletViewModel_Factory create(
      Provider<WalletRepository> walletRepositoryProvider) {
    return new WalletViewModel_Factory(walletRepositoryProvider);
  }

  public static WalletViewModel newInstance(WalletRepository walletRepository) {
    return new WalletViewModel(walletRepository);
  }
}
