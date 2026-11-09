package com.spacetime.evcharging.feature.auth.onboarding;

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
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<UserPreferences> userPreferencesProvider;

  public OnboardingViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.userPreferencesProvider = userPreferencesProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(authRepositoryProvider.get(), userPreferencesProvider.get());
  }

  public static OnboardingViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    return new OnboardingViewModel_Factory(authRepositoryProvider, userPreferencesProvider);
  }

  public static OnboardingViewModel newInstance(AuthRepository authRepository,
      UserPreferences userPreferences) {
    return new OnboardingViewModel(authRepository, userPreferences);
  }
}
