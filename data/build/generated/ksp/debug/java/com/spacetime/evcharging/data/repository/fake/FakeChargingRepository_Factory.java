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
public final class FakeChargingRepository_Factory implements Factory<FakeChargingRepository> {
  @Override
  public FakeChargingRepository get() {
    return newInstance();
  }

  public static FakeChargingRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FakeChargingRepository newInstance() {
    return new FakeChargingRepository();
  }

  private static final class InstanceHolder {
    private static final FakeChargingRepository_Factory INSTANCE = new FakeChargingRepository_Factory();
  }
}
