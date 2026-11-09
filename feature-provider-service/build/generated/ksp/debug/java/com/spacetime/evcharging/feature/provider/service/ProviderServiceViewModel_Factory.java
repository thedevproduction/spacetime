package com.spacetime.evcharging.feature.provider.service;

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
public final class ProviderServiceViewModel_Factory implements Factory<ProviderServiceViewModel> {
  private final Provider<ServiceProviderRepository> serviceProviderRepositoryProvider;

  public ProviderServiceViewModel_Factory(
      Provider<ServiceProviderRepository> serviceProviderRepositoryProvider) {
    this.serviceProviderRepositoryProvider = serviceProviderRepositoryProvider;
  }

  @Override
  public ProviderServiceViewModel get() {
    return newInstance(serviceProviderRepositoryProvider.get());
  }

  public static ProviderServiceViewModel_Factory create(
      Provider<ServiceProviderRepository> serviceProviderRepositoryProvider) {
    return new ProviderServiceViewModel_Factory(serviceProviderRepositoryProvider);
  }

  public static ProviderServiceViewModel newInstance(
      ServiceProviderRepository serviceProviderRepository) {
    return new ProviderServiceViewModel(serviceProviderRepository);
  }
}
