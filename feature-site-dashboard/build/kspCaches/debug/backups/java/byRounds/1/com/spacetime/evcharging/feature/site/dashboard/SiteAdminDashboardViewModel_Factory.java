package com.spacetime.evcharging.feature.site.dashboard;

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
public final class SiteAdminDashboardViewModel_Factory implements Factory<SiteAdminDashboardViewModel> {
  private final Provider<SiteAdminRepository> siteAdminRepositoryProvider;

  public SiteAdminDashboardViewModel_Factory(
      Provider<SiteAdminRepository> siteAdminRepositoryProvider) {
    this.siteAdminRepositoryProvider = siteAdminRepositoryProvider;
  }

  @Override
  public SiteAdminDashboardViewModel get() {
    return newInstance(siteAdminRepositoryProvider.get());
  }

  public static SiteAdminDashboardViewModel_Factory create(
      Provider<SiteAdminRepository> siteAdminRepositoryProvider) {
    return new SiteAdminDashboardViewModel_Factory(siteAdminRepositoryProvider);
  }

  public static SiteAdminDashboardViewModel newInstance(SiteAdminRepository siteAdminRepository) {
    return new SiteAdminDashboardViewModel(siteAdminRepository);
  }
}
