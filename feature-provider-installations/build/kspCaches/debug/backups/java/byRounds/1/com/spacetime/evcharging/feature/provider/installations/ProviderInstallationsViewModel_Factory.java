package com.spacetime.evcharging.feature.provider.installations;

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
public final class ProviderInstallationsViewModel_Factory implements Factory<ProviderInstallationsViewModel> {
  private final Provider<ServiceProviderRepository> serviceProviderRepositoryProvider;

  public ProviderInstallationsViewModel_Factory(
      Provider<ServiceProviderRepository> serviceProviderRepositoryProvider) {
    this.serviceProviderRepositoryProvider = serviceProviderRepositoryProvider;
  }

  @Override
  public ProviderInstallationsViewModel get() {
    return newInstance(serviceProviderRepositoryProvider.get());
  }

  public static ProviderInstallationsViewModel_Factory create(
      Provider<ServiceProviderRepository> serviceProviderRepositoryProvider) {
    return new ProviderInstallationsViewModel_Factory(serviceProviderRepositoryProvider);
  }

  public static ProviderInstallationsViewModel newInstance(
      ServiceProviderRepository serviceProviderRepository) {
    return new ProviderInstallationsViewModel(serviceProviderRepository);
  }
}
