package com.spacetime.evcharging.feature.station.details;

import androidx.lifecycle.SavedStateHandle;
import com.spacetime.evcharging.data.repository.StationRepository;
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
public final class StationDetailsViewModel_Factory implements Factory<StationDetailsViewModel> {
  private final Provider<StationRepository> stationRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public StationDetailsViewModel_Factory(Provider<StationRepository> stationRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.stationRepositoryProvider = stationRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public StationDetailsViewModel get() {
    return newInstance(stationRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static StationDetailsViewModel_Factory create(
      Provider<StationRepository> stationRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new StationDetailsViewModel_Factory(stationRepositoryProvider, savedStateHandleProvider);
  }

  public static StationDetailsViewModel newInstance(StationRepository stationRepository,
      SavedStateHandle savedStateHandle) {
    return new StationDetailsViewModel(stationRepository, savedStateHandle);
  }
}
