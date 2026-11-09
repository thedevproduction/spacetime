package com.spacetime.evcharging.core.network.di;

import com.spacetime.evcharging.core.network.interceptor.AuthInterceptor;
import com.spacetime.evcharging.core.network.interceptor.RequestIdInterceptor;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

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
public final class NetworkModule_ProvideOkHttpClientFactory implements Factory<OkHttpClient> {
  private final Provider<AuthInterceptor> authInterceptorProvider;

  private final Provider<RequestIdInterceptor> requestIdInterceptorProvider;

  public NetworkModule_ProvideOkHttpClientFactory(Provider<AuthInterceptor> authInterceptorProvider,
      Provider<RequestIdInterceptor> requestIdInterceptorProvider) {
    this.authInterceptorProvider = authInterceptorProvider;
    this.requestIdInterceptorProvider = requestIdInterceptorProvider;
  }

  @Override
  public OkHttpClient get() {
    return provideOkHttpClient(authInterceptorProvider.get(), requestIdInterceptorProvider.get());
  }

  public static NetworkModule_ProvideOkHttpClientFactory create(
      Provider<AuthInterceptor> authInterceptorProvider,
      Provider<RequestIdInterceptor> requestIdInterceptorProvider) {
    return new NetworkModule_ProvideOkHttpClientFactory(authInterceptorProvider, requestIdInterceptorProvider);
  }

  public static OkHttpClient provideOkHttpClient(AuthInterceptor authInterceptor,
      RequestIdInterceptor requestIdInterceptor) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideOkHttpClient(authInterceptor, requestIdInterceptor));
  }
}
