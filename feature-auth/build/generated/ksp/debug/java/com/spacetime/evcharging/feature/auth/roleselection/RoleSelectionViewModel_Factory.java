package com.spacetime.evcharging.feature.auth.roleselection;

import com.spacetime.evcharging.core.datastore.UserPreferences;
import com.spacetime.evcharging.data.repository.AuthRepository;
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
public final class RoleSelectionViewModel_Factory implements Factory<RoleSelectionViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<UserPreferences> userPreferencesProvider;

  public RoleSelectionViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.userPreferencesProvider = userPreferencesProvider;
  }

  @Override
  public RoleSelectionViewModel get() {
    return newInstance(authRepositoryProvider.get(), userPreferencesProvider.get());
  }

  public static RoleSelectionViewModel_Factory create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    return new RoleSelectionViewModel_Factory(authRepositoryProvider, userPreferencesProvider);
  }

  public static RoleSelectionViewModel newInstance(AuthRepository authRepository,
      UserPreferences userPreferences) {
    return new RoleSelectionViewModel(authRepository, userPreferences);
  }
}
