package com.spacetime.evcharging.feature.auth.otp;

import androidx.lifecycle.SavedStateHandle;
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
public final class OtpVerificationViewModel_Factory implements Factory<OtpVerificationViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public OtpVerificationViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public OtpVerificationViewModel get() {
    return newInstance(authRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static OtpVerificationViewModel_Factory create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new OtpVerificationViewModel_Factory(authRepositoryProvider, savedStateHandleProvider);
  }

  public static OtpVerificationViewModel newInstance(AuthRepository authRepository,
      SavedStateHandle savedStateHandle) {
    return new OtpVerificationViewModel(authRepository, savedStateHandle);
  }
}
