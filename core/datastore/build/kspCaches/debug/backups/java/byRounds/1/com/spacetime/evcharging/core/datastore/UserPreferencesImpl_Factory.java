package com.spacetime.evcharging.core.datastore;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class UserPreferencesImpl_Factory implements Factory<UserPreferencesImpl> {
  private final Provider<Context> contextProvider;

  public UserPreferencesImpl_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public UserPreferencesImpl get() {
    return newInstance(contextProvider.get());
  }

  public static UserPreferencesImpl_Factory create(Provider<Context> contextProvider) {
    return new UserPreferencesImpl_Factory(contextProvider);
  }

  public static UserPreferencesImpl newInstance(Context context) {
    return new UserPreferencesImpl(context);
  }
}
