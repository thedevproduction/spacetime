package com.spacetime.evcharging.core.network.interceptor;

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
public final class RequestIdInterceptor_Factory implements Factory<RequestIdInterceptor> {
  @Override
  public RequestIdInterceptor get() {
    return newInstance();
  }

  public static RequestIdInterceptor_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RequestIdInterceptor newInstance() {
    return new RequestIdInterceptor();
  }

  private static final class InstanceHolder {
    private static final RequestIdInterceptor_Factory INSTANCE = new RequestIdInterceptor_Factory();
  }
}
