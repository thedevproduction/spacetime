package com.spacetime.evcharging.feature.customer.profile;

import com.spacetime.evcharging.core.datastore.UserPreferences;
import com.spacetime.evcharging.data.repository.AuthRepository;
import com.spacetime.evcharging.data.repository.StationRepository;
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<StationRepository> stationRepositoryProvider;

  private final Provider<UserPreferences> userPreferencesProvider;

  public ProfileViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<StationRepository> stationRepositoryProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.stationRepositoryProvider = stationRepositoryProvider;
    this.userPreferencesProvider = userPreferencesProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(authRepositoryProvider.get(), stationRepositoryProvider.get(), userPreferencesProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<StationRepository> stationRepositoryProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    return new ProfileViewModel_Factory(authRepositoryProvider, stationRepositoryProvider, userPreferencesProvider);
  }

  public static ProfileViewModel newInstance(AuthRepository authRepository,
      StationRepository stationRepository, UserPreferences userPreferences) {
    return new ProfileViewModel(authRepository, stationRepository, userPreferences);
  }
}
