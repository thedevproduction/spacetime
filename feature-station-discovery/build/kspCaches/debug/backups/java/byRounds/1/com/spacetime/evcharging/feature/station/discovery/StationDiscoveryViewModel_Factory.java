package com.spacetime.evcharging.feature.station.discovery;

import com.spacetime.evcharging.data.repository.StationRepository;
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
public final class StationDiscoveryViewModel_Factory implements Factory<StationDiscoveryViewModel> {
  private final Provider<StationRepository> stationRepositoryProvider;

  public StationDiscoveryViewModel_Factory(Provider<StationRepository> stationRepositoryProvider) {
    this.stationRepositoryProvider = stationRepositoryProvider;
  }

  @Override
  public StationDiscoveryViewModel get() {
    return newInstance(stationRepositoryProvider.get());
  }

  public static StationDiscoveryViewModel_Factory create(
      Provider<StationRepository> stationRepositoryProvider) {
    return new StationDiscoveryViewModel_Factory(stationRepositoryProvider);
  }

  public static StationDiscoveryViewModel newInstance(StationRepository stationRepository) {
    return new StationDiscoveryViewModel(stationRepository);
  }
}
