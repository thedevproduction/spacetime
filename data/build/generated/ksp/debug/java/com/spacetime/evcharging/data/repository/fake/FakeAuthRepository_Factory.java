package com.spacetime.evcharging.data.repository.fake;

import com.spacetime.evcharging.core.security.TokenManager;
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
public final class FakeAuthRepository_Factory implements Factory<FakeAuthRepository> {
  private final Provider<TokenManager> tokenManagerProvider;

  public FakeAuthRepository_Factory(Provider<TokenManager> tokenManagerProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public FakeAuthRepository get() {
    return newInstance(tokenManagerProvider.get());
  }

  public static FakeAuthRepository_Factory create(Provider<TokenManager> tokenManagerProvider) {
    return new FakeAuthRepository_Factory(tokenManagerProvider);
  }

  public static FakeAuthRepository newInstance(TokenManager tokenManager) {
    return new FakeAuthRepository(tokenManager);
  }
}
