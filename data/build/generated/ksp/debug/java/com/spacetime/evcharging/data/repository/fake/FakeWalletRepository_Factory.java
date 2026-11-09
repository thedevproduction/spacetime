package com.spacetime.evcharging.data.repository.fake;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class FakeWalletRepository_Factory implements Factory<FakeWalletRepository> {
  @Override
  public FakeWalletRepository get() {
    return newInstance();
  }

  public static FakeWalletRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FakeWalletRepository newInstance() {
    return new FakeWalletRepository();
  }

  private static final class InstanceHolder {
    private static final FakeWalletRepository_Factory INSTANCE = new FakeWalletRepository_Factory();
  }
}
