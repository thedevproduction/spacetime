package com.spacetime.evcharging.feature.site.stations;

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
public final class SiteStationsViewModel_Factory implements Factory<SiteStationsViewModel> {
  private final Provider<SiteAdminRepository> siteAdminRepositoryProvider;

  public SiteStationsViewModel_Factory(Provider<SiteAdminRepository> siteAdminRepositoryProvider) {
    this.siteAdminRepositoryProvider = siteAdminRepositoryProvider;
  }

  @Override
  public SiteStationsViewModel get() {
    return newInstance(siteAdminRepositoryProvider.get());
  }

  public static SiteStationsViewModel_Factory create(
      Provider<SiteAdminRepository> siteAdminRepositoryProvider) {
    return new SiteStationsViewModel_Factory(siteAdminRepositoryProvider);
  }

  public static SiteStationsViewModel newInstance(SiteAdminRepository siteAdminRepository) {
    return new SiteStationsViewModel(siteAdminRepository);
  }
}
