package com.spacetime.evcharging.feature.qr.scanner;

import com.spacetime.evcharging.data.repository.ChargingRepository;
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
public final class QrScannerViewModel_Factory implements Factory<QrScannerViewModel> {
  private final Provider<ChargingRepository> chargingRepositoryProvider;

  public QrScannerViewModel_Factory(Provider<ChargingRepository> chargingRepositoryProvider) {
    this.chargingRepositoryProvider = chargingRepositoryProvider;
  }

  @Override
  public QrScannerViewModel get() {
    return newInstance(chargingRepositoryProvider.get());
  }

  public static QrScannerViewModel_Factory create(
      Provider<ChargingRepository> chargingRepositoryProvider) {
    return new QrScannerViewModel_Factory(chargingRepositoryProvider);
  }

  public static QrScannerViewModel newInstance(ChargingRepository chargingRepository) {
    return new QrScannerViewModel(chargingRepository);
  }
}
