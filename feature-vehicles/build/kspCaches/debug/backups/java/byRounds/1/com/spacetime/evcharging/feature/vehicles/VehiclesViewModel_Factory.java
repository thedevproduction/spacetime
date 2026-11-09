package com.spacetime.evcharging.feature.vehicles;

import com.spacetime.evcharging.data.repository.VehicleRepository;
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
public final class VehiclesViewModel_Factory implements Factory<VehiclesViewModel> {
  private final Provider<VehicleRepository> vehicleRepositoryProvider;

  public VehiclesViewModel_Factory(Provider<VehicleRepository> vehicleRepositoryProvider) {
    this.vehicleRepositoryProvider = vehicleRepositoryProvider;
  }

  @Override
  public VehiclesViewModel get() {
    return newInstance(vehicleRepositoryProvider.get());
  }

  public static VehiclesViewModel_Factory create(
      Provider<VehicleRepository> vehicleRepositoryProvider) {
    return new VehiclesViewModel_Factory(vehicleRepositoryProvider);
  }

  public static VehiclesViewModel newInstance(VehicleRepository vehicleRepository) {
    return new VehiclesViewModel(vehicleRepository);
  }
}
