package com.spacetime.evcharging.feature.provider.technicians;

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
public final class ProviderTechniciansViewModel_Factory implements Factory<ProviderTechniciansViewModel> {
  private final Provider<ServiceProviderRepository> serviceProviderRepositoryProvider;

  public ProviderTechniciansViewModel_Factory(
      Provider<ServiceProviderRepository> serviceProviderRepositoryProvider) {
    this.serviceProviderRepositoryProvider = serviceProviderRepositoryProvider;
  }

  @Override
  public ProviderTechniciansViewModel get() {
    return newInstance(serviceProviderRepositoryProvider.get());
  }

  public static ProviderTechniciansViewModel_Factory create(
      Provider<ServiceProviderRepository> serviceProviderRepositoryProvider) {
    return new ProviderTechniciansViewModel_Factory(serviceProviderRepositoryProvider);
  }

  public static ProviderTechniciansViewModel newInstance(
      ServiceProviderRepository serviceProviderRepository) {
    return new ProviderTechniciansViewModel(serviceProviderRepository);
  }
}
