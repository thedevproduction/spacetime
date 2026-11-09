package com.spacetime.evcharging.feature.site.employees;

import com.spacetime.evcharging.data.repository.SiteAdminRepository;
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
public final class SiteEmployeesViewModel_Factory implements Factory<SiteEmployeesViewModel> {
  private final Provider<SiteAdminRepository> siteAdminRepositoryProvider;

  public SiteEmployeesViewModel_Factory(Provider<SiteAdminRepository> siteAdminRepositoryProvider) {
    this.siteAdminRepositoryProvider = siteAdminRepositoryProvider;
  }

  @Override
  public SiteEmployeesViewModel get() {
    return newInstance(siteAdminRepositoryProvider.get());
  }

  public static SiteEmployeesViewModel_Factory create(
      Provider<SiteAdminRepository> siteAdminRepositoryProvider) {
    return new SiteEmployeesViewModel_Factory(siteAdminRepositoryProvider);
  }

  public static SiteEmployeesViewModel newInstance(SiteAdminRepository siteAdminRepository) {
    return new SiteEmployeesViewModel(siteAdminRepository);
  }
}
