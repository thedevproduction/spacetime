package com.spacetime.evcharging.feature.site.sessions;

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
public final class SiteSessionsViewModel_Factory implements Factory<SiteSessionsViewModel> {
  private final Provider<SiteAdminRepository> siteAdminRepositoryProvider;

  public SiteSessionsViewModel_Factory(Provider<SiteAdminRepository> siteAdminRepositoryProvider) {
    this.siteAdminRepositoryProvider = siteAdminRepositoryProvider;
  }

  @Override
  public SiteSessionsViewModel get() {
    return newInstance(siteAdminRepositoryProvider.get());
  }

  public static SiteSessionsViewModel_Factory create(
      Provider<SiteAdminRepository> siteAdminRepositoryProvider) {
    return new SiteSessionsViewModel_Factory(siteAdminRepositoryProvider);
  }

  public static SiteSessionsViewModel newInstance(SiteAdminRepository siteAdminRepository) {
    return new SiteSessionsViewModel(siteAdminRepository);
  }
}
