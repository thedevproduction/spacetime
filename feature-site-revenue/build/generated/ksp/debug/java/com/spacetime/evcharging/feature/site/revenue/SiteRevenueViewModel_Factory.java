package com.spacetime.evcharging.feature.site.revenue;

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
public final class SiteRevenueViewModel_Factory implements Factory<SiteRevenueViewModel> {
  private final Provider<SiteAdminRepository> siteAdminRepositoryProvider;

  public SiteRevenueViewModel_Factory(Provider<SiteAdminRepository> siteAdminRepositoryProvider) {
    this.siteAdminRepositoryProvider = siteAdminRepositoryProvider;
  }

  @Override
  public SiteRevenueViewModel get() {
    return newInstance(siteAdminRepositoryProvider.get());
  }

  public static SiteRevenueViewModel_Factory create(
      Provider<SiteAdminRepository> siteAdminRepositoryProvider) {
    return new SiteRevenueViewModel_Factory(siteAdminRepositoryProvider);
  }

  public static SiteRevenueViewModel newInstance(SiteAdminRepository siteAdminRepository) {
    return new SiteRevenueViewModel(siteAdminRepository);
  }
}
