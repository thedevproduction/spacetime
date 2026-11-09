package com.spacetime.evcharging.data.repository.fake;

import com.spacetime.evcharging.data.repository.StationRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class FakeSiteAdminRepository_Factory implements Factory<FakeSiteAdminRepository> {
  private final Provider<StationRepository> stationRepositoryProvider;

  public FakeSiteAdminRepository_Factory(Provider<StationRepository> stationRepositoryProvider) {
    this.stationRepositoryProvider = stationRepositoryProvider;
  }

  @Override
  public FakeSiteAdminRepository get() {
    return newInstance(stationRepositoryProvider.get());
  }

  public static FakeSiteAdminRepository_Factory create(
      Provider<StationRepository> stationRepositoryProvider) {
    return new FakeSiteAdminRepository_Factory(stationRepositoryProvider);
  }

  public static FakeSiteAdminRepository newInstance(StationRepository stationRepository) {
    return new FakeSiteAdminRepository(stationRepository);
  }
}
