package com.spacetime.evcharging.feature.provider.dashboard;

import com.spacetime.evcharging.data.repository.ServiceProviderRepository;
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
public final class ProviderDashboardViewModel_Factory implements Factory<ProviderDashboardViewModel> {
  private final Provider<ServiceProviderRepository> serviceProviderRepositoryProvider;

  public ProviderDashboardViewModel_Factory(
      Provider<ServiceProviderRepository> serviceProviderRepositoryProvider) {
    this.serviceProviderRepositoryProvider = serviceProviderRepositoryProvider;
  }

  @Override
  public ProviderDashboardViewModel get() {
    return newInstance(serviceProviderRepositoryProvider.get());
  }

  public static ProviderDashboardViewModel_Factory create(
      Provider<ServiceProviderRepository> serviceProviderRepositoryProvider) {
    return new ProviderDashboardViewModel_Factory(serviceProviderRepositoryProvider);
  }

  public static ProviderDashboardViewModel newInstance(
      ServiceProviderRepository serviceProviderRepository) {
    return new ProviderDashboardViewModel(serviceProviderRepository);
  }
}
