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
public final class FakeStationRepository_Factory implements Factory<FakeStationRepository> {
  @Override
  public FakeStationRepository get() {
    return newInstance();
  }

  public static FakeStationRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FakeStationRepository newInstance() {
    return new FakeStationRepository();
  }

  private static final class InstanceHolder {
    private static final FakeStationRepository_Factory INSTANCE = new FakeStationRepository_Factory();
  }
}
