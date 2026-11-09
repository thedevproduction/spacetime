package com.spacetime.evcharging;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.spacetime.evcharging.core.datastore.UserPreferencesImpl;
import com.spacetime.evcharging.core.security.TokenManagerImpl;
import com.spacetime.evcharging.data.repository.fake.FakeAuthRepository;
import com.spacetime.evcharging.data.repository.fake.FakeChargingRepository;
import com.spacetime.evcharging.data.repository.fake.FakeServiceProviderRepository;
import com.spacetime.evcharging.data.repository.fake.FakeSiteAdminRepository;
import com.spacetime.evcharging.data.repository.fake.FakeStationRepository;
import com.spacetime.evcharging.data.repository.fake.FakeVehicleRepository;
import com.spacetime.evcharging.data.repository.fake.FakeWalletRepository;
import com.spacetime.evcharging.feature.auth.onboarding.OnboardingViewModel;
import com.spacetime.evcharging.feature.auth.onboarding.OnboardingViewModel_HiltModules;
import com.spacetime.evcharging.feature.auth.otp.OtpVerificationViewModel;
import com.spacetime.evcharging.feature.auth.otp.OtpVerificationViewModel_HiltModules;
import com.spacetime.evcharging.feature.auth.roleselection.RoleSelectionViewModel;
import com.spacetime.evcharging.feature.auth.roleselection.RoleSelectionViewModel_HiltModules;
import com.spacetime.evcharging.feature.auth.splash.SplashViewModel;
import com.spacetime.evcharging.feature.auth.splash.SplashViewModel_HiltModules;
import com.spacetime.evcharging.feature.auth.welcome.WelcomeViewModel;
import com.spacetime.evcharging.feature.auth.welcome.WelcomeViewModel_HiltModules;
import com.spacetime.evcharging.feature.charging.ChargingViewModel;
import com.spacetime.evcharging.feature.charging.ChargingViewModel_HiltModules;
import com.spacetime.evcharging.feature.customer.home.CustomerHomeViewModel;
import com.spacetime.evcharging.feature.customer.home.CustomerHomeViewModel_HiltModules;
import com.spacetime.evcharging.feature.customer.profile.ProfileViewModel;
import com.spacetime.evcharging.feature.customer.profile.ProfileViewModel_HiltModules;
import com.spacetime.evcharging.feature.provider.dashboard.ProviderDashboardViewModel;
import com.spacetime.evcharging.feature.provider.dashboard.ProviderDashboardViewModel_HiltModules;
import com.spacetime.evcharging.feature.provider.equipment.ProviderEquipmentViewModel;
import com.spacetime.evcharging.feature.provider.equipment.ProviderEquipmentViewModel_HiltModules;
import com.spacetime.evcharging.feature.provider.installations.ProviderInstallationsViewModel;
import com.spacetime.evcharging.feature.provider.installations.ProviderInstallationsViewModel_HiltModules;
import com.spacetime.evcharging.feature.provider.service.ProviderServiceViewModel;
import com.spacetime.evcharging.feature.provider.service.ProviderServiceViewModel_HiltModules;
import com.spacetime.evcharging.feature.provider.technicians.ProviderTechniciansViewModel;
import com.spacetime.evcharging.feature.provider.technicians.ProviderTechniciansViewModel_HiltModules;
import com.spacetime.evcharging.feature.qr.scanner.QrScannerViewModel;
import com.spacetime.evcharging.feature.qr.scanner.QrScannerViewModel_HiltModules;
import com.spacetime.evcharging.feature.site.dashboard.SiteAdminDashboardViewModel;
import com.spacetime.evcharging.feature.site.dashboard.SiteAdminDashboardViewModel_HiltModules;
import com.spacetime.evcharging.feature.site.employees.SiteEmployeesViewModel;
import com.spacetime.evcharging.feature.site.employees.SiteEmployeesViewModel_HiltModules;
import com.spacetime.evcharging.feature.site.revenue.SiteRevenueViewModel;
import com.spacetime.evcharging.feature.site.revenue.SiteRevenueViewModel_HiltModules;
import com.spacetime.evcharging.feature.site.sessions.SiteSessionsViewModel;
import com.spacetime.evcharging.feature.site.sessions.SiteSessionsViewModel_HiltModules;
import com.spacetime.evcharging.feature.site.stations.SiteStationsViewModel;
import com.spacetime.evcharging.feature.site.stations.SiteStationsViewModel_HiltModules;
import com.spacetime.evcharging.feature.station.details.StationDetailsViewModel;
import com.spacetime.evcharging.feature.station.details.StationDetailsViewModel_HiltModules;
import com.spacetime.evcharging.feature.station.discovery.StationDiscoveryViewModel;
import com.spacetime.evcharging.feature.station.discovery.StationDiscoveryViewModel_HiltModules;
import com.spacetime.evcharging.feature.vehicles.VehiclesViewModel;
import com.spacetime.evcharging.feature.vehicles.VehiclesViewModel_HiltModules;
import com.spacetime.evcharging.feature.wallet.WalletViewModel;
import com.spacetime.evcharging.feature.wallet.WalletViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerSpacetimeApp_HiltComponents_SingletonC {
  private DaggerSpacetimeApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public SpacetimeApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements SpacetimeApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public SpacetimeApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements SpacetimeApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public SpacetimeApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements SpacetimeApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public SpacetimeApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements SpacetimeApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public SpacetimeApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements SpacetimeApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public SpacetimeApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements SpacetimeApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public SpacetimeApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements SpacetimeApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public SpacetimeApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends SpacetimeApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends SpacetimeApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends SpacetimeApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends SpacetimeApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(23).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_charging_ChargingViewModel, ChargingViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_customer_home_CustomerHomeViewModel, CustomerHomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_auth_onboarding_OnboardingViewModel, OnboardingViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_auth_otp_OtpVerificationViewModel, OtpVerificationViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_customer_profile_ProfileViewModel, ProfileViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_provider_dashboard_ProviderDashboardViewModel, ProviderDashboardViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_provider_equipment_ProviderEquipmentViewModel, ProviderEquipmentViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_provider_installations_ProviderInstallationsViewModel, ProviderInstallationsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_provider_service_ProviderServiceViewModel, ProviderServiceViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_provider_technicians_ProviderTechniciansViewModel, ProviderTechniciansViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_qr_scanner_QrScannerViewModel, QrScannerViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_auth_roleselection_RoleSelectionViewModel, RoleSelectionViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_site_dashboard_SiteAdminDashboardViewModel, SiteAdminDashboardViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_site_employees_SiteEmployeesViewModel, SiteEmployeesViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_site_revenue_SiteRevenueViewModel, SiteRevenueViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_site_sessions_SiteSessionsViewModel, SiteSessionsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_site_stations_SiteStationsViewModel, SiteStationsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_auth_splash_SplashViewModel, SplashViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_station_details_StationDetailsViewModel, StationDetailsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_station_discovery_StationDiscoveryViewModel, StationDiscoveryViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_vehicles_VehiclesViewModel, VehiclesViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_wallet_WalletViewModel, WalletViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_auth_welcome_WelcomeViewModel, WelcomeViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_spacetime_evcharging_feature_vehicles_VehiclesViewModel = "com.spacetime.evcharging.feature.vehicles.VehiclesViewModel";

      static String com_spacetime_evcharging_feature_station_details_StationDetailsViewModel = "com.spacetime.evcharging.feature.station.details.StationDetailsViewModel";

      static String com_spacetime_evcharging_feature_site_dashboard_SiteAdminDashboardViewModel = "com.spacetime.evcharging.feature.site.dashboard.SiteAdminDashboardViewModel";

      static String com_spacetime_evcharging_feature_customer_profile_ProfileViewModel = "com.spacetime.evcharging.feature.customer.profile.ProfileViewModel";

      static String com_spacetime_evcharging_feature_station_discovery_StationDiscoveryViewModel = "com.spacetime.evcharging.feature.station.discovery.StationDiscoveryViewModel";

      static String com_spacetime_evcharging_feature_auth_otp_OtpVerificationViewModel = "com.spacetime.evcharging.feature.auth.otp.OtpVerificationViewModel";

      static String com_spacetime_evcharging_feature_auth_onboarding_OnboardingViewModel = "com.spacetime.evcharging.feature.auth.onboarding.OnboardingViewModel";

      static String com_spacetime_evcharging_feature_site_employees_SiteEmployeesViewModel = "com.spacetime.evcharging.feature.site.employees.SiteEmployeesViewModel";

      static String com_spacetime_evcharging_feature_provider_service_ProviderServiceViewModel = "com.spacetime.evcharging.feature.provider.service.ProviderServiceViewModel";

      static String com_spacetime_evcharging_feature_charging_ChargingViewModel = "com.spacetime.evcharging.feature.charging.ChargingViewModel";

      static String com_spacetime_evcharging_feature_qr_scanner_QrScannerViewModel = "com.spacetime.evcharging.feature.qr.scanner.QrScannerViewModel";

      static String com_spacetime_evcharging_feature_site_stations_SiteStationsViewModel = "com.spacetime.evcharging.feature.site.stations.SiteStationsViewModel";

      static String com_spacetime_evcharging_feature_wallet_WalletViewModel = "com.spacetime.evcharging.feature.wallet.WalletViewModel";

      static String com_spacetime_evcharging_feature_provider_dashboard_ProviderDashboardViewModel = "com.spacetime.evcharging.feature.provider.dashboard.ProviderDashboardViewModel";

      static String com_spacetime_evcharging_feature_auth_roleselection_RoleSelectionViewModel = "com.spacetime.evcharging.feature.auth.roleselection.RoleSelectionViewModel";

      static String com_spacetime_evcharging_feature_site_revenue_SiteRevenueViewModel = "com.spacetime.evcharging.feature.site.revenue.SiteRevenueViewModel";

      static String com_spacetime_evcharging_feature_auth_splash_SplashViewModel = "com.spacetime.evcharging.feature.auth.splash.SplashViewModel";

      static String com_spacetime_evcharging_feature_provider_equipment_ProviderEquipmentViewModel = "com.spacetime.evcharging.feature.provider.equipment.ProviderEquipmentViewModel";

      static String com_spacetime_evcharging_feature_customer_home_CustomerHomeViewModel = "com.spacetime.evcharging.feature.customer.home.CustomerHomeViewModel";

      static String com_spacetime_evcharging_feature_provider_installations_ProviderInstallationsViewModel = "com.spacetime.evcharging.feature.provider.installations.ProviderInstallationsViewModel";

      static String com_spacetime_evcharging_feature_provider_technicians_ProviderTechniciansViewModel = "com.spacetime.evcharging.feature.provider.technicians.ProviderTechniciansViewModel";

      static String com_spacetime_evcharging_feature_site_sessions_SiteSessionsViewModel = "com.spacetime.evcharging.feature.site.sessions.SiteSessionsViewModel";

      static String com_spacetime_evcharging_feature_auth_welcome_WelcomeViewModel = "com.spacetime.evcharging.feature.auth.welcome.WelcomeViewModel";

      @KeepFieldType
      VehiclesViewModel com_spacetime_evcharging_feature_vehicles_VehiclesViewModel2;

      @KeepFieldType
      StationDetailsViewModel com_spacetime_evcharging_feature_station_details_StationDetailsViewModel2;

      @KeepFieldType
      SiteAdminDashboardViewModel com_spacetime_evcharging_feature_site_dashboard_SiteAdminDashboardViewModel2;

      @KeepFieldType
      ProfileViewModel com_spacetime_evcharging_feature_customer_profile_ProfileViewModel2;

      @KeepFieldType
      StationDiscoveryViewModel com_spacetime_evcharging_feature_station_discovery_StationDiscoveryViewModel2;

      @KeepFieldType
      OtpVerificationViewModel com_spacetime_evcharging_feature_auth_otp_OtpVerificationViewModel2;

      @KeepFieldType
      OnboardingViewModel com_spacetime_evcharging_feature_auth_onboarding_OnboardingViewModel2;

      @KeepFieldType
      SiteEmployeesViewModel com_spacetime_evcharging_feature_site_employees_SiteEmployeesViewModel2;

      @KeepFieldType
      ProviderServiceViewModel com_spacetime_evcharging_feature_provider_service_ProviderServiceViewModel2;

      @KeepFieldType
      ChargingViewModel com_spacetime_evcharging_feature_charging_ChargingViewModel2;

      @KeepFieldType
      QrScannerViewModel com_spacetime_evcharging_feature_qr_scanner_QrScannerViewModel2;

      @KeepFieldType
      SiteStationsViewModel com_spacetime_evcharging_feature_site_stations_SiteStationsViewModel2;

      @KeepFieldType
      WalletViewModel com_spacetime_evcharging_feature_wallet_WalletViewModel2;

      @KeepFieldType
      ProviderDashboardViewModel com_spacetime_evcharging_feature_provider_dashboard_ProviderDashboardViewModel2;

      @KeepFieldType
      RoleSelectionViewModel com_spacetime_evcharging_feature_auth_roleselection_RoleSelectionViewModel2;

      @KeepFieldType
      SiteRevenueViewModel com_spacetime_evcharging_feature_site_revenue_SiteRevenueViewModel2;

      @KeepFieldType
      SplashViewModel com_spacetime_evcharging_feature_auth_splash_SplashViewModel2;

      @KeepFieldType
      ProviderEquipmentViewModel com_spacetime_evcharging_feature_provider_equipment_ProviderEquipmentViewModel2;

      @KeepFieldType
      CustomerHomeViewModel com_spacetime_evcharging_feature_customer_home_CustomerHomeViewModel2;

      @KeepFieldType
      ProviderInstallationsViewModel com_spacetime_evcharging_feature_provider_installations_ProviderInstallationsViewModel2;

      @KeepFieldType
      ProviderTechniciansViewModel com_spacetime_evcharging_feature_provider_technicians_ProviderTechniciansViewModel2;

      @KeepFieldType
      SiteSessionsViewModel com_spacetime_evcharging_feature_site_sessions_SiteSessionsViewModel2;

      @KeepFieldType
      WelcomeViewModel com_spacetime_evcharging_feature_auth_welcome_WelcomeViewModel2;
    }
  }

  private static final class ViewModelCImpl extends SpacetimeApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<ChargingViewModel> chargingViewModelProvider;

    private Provider<CustomerHomeViewModel> customerHomeViewModelProvider;

    private Provider<OnboardingViewModel> onboardingViewModelProvider;

    private Provider<OtpVerificationViewModel> otpVerificationViewModelProvider;

    private Provider<ProfileViewModel> profileViewModelProvider;

    private Provider<ProviderDashboardViewModel> providerDashboardViewModelProvider;

    private Provider<ProviderEquipmentViewModel> providerEquipmentViewModelProvider;

    private Provider<ProviderInstallationsViewModel> providerInstallationsViewModelProvider;

    private Provider<ProviderServiceViewModel> providerServiceViewModelProvider;

    private Provider<ProviderTechniciansViewModel> providerTechniciansViewModelProvider;

    private Provider<QrScannerViewModel> qrScannerViewModelProvider;

    private Provider<RoleSelectionViewModel> roleSelectionViewModelProvider;

    private Provider<SiteAdminDashboardViewModel> siteAdminDashboardViewModelProvider;

    private Provider<SiteEmployeesViewModel> siteEmployeesViewModelProvider;

    private Provider<SiteRevenueViewModel> siteRevenueViewModelProvider;

    private Provider<SiteSessionsViewModel> siteSessionsViewModelProvider;

    private Provider<SiteStationsViewModel> siteStationsViewModelProvider;

    private Provider<SplashViewModel> splashViewModelProvider;

    private Provider<StationDetailsViewModel> stationDetailsViewModelProvider;

    private Provider<StationDiscoveryViewModel> stationDiscoveryViewModelProvider;

    private Provider<VehiclesViewModel> vehiclesViewModelProvider;

    private Provider<WalletViewModel> walletViewModelProvider;

    private Provider<WelcomeViewModel> welcomeViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.chargingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.customerHomeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.onboardingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.otpVerificationViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.profileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.providerDashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.providerEquipmentViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.providerInstallationsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.providerServiceViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.providerTechniciansViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.qrScannerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
      this.roleSelectionViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 11);
      this.siteAdminDashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 12);
      this.siteEmployeesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 13);
      this.siteRevenueViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 14);
      this.siteSessionsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 15);
      this.siteStationsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 16);
      this.splashViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 17);
      this.stationDetailsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 18);
      this.stationDiscoveryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 19);
      this.vehiclesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 20);
      this.walletViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 21);
      this.welcomeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 22);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(23).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_charging_ChargingViewModel, ((Provider) chargingViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_customer_home_CustomerHomeViewModel, ((Provider) customerHomeViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_auth_onboarding_OnboardingViewModel, ((Provider) onboardingViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_auth_otp_OtpVerificationViewModel, ((Provider) otpVerificationViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_customer_profile_ProfileViewModel, ((Provider) profileViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_provider_dashboard_ProviderDashboardViewModel, ((Provider) providerDashboardViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_provider_equipment_ProviderEquipmentViewModel, ((Provider) providerEquipmentViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_provider_installations_ProviderInstallationsViewModel, ((Provider) providerInstallationsViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_provider_service_ProviderServiceViewModel, ((Provider) providerServiceViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_provider_technicians_ProviderTechniciansViewModel, ((Provider) providerTechniciansViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_qr_scanner_QrScannerViewModel, ((Provider) qrScannerViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_auth_roleselection_RoleSelectionViewModel, ((Provider) roleSelectionViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_site_dashboard_SiteAdminDashboardViewModel, ((Provider) siteAdminDashboardViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_site_employees_SiteEmployeesViewModel, ((Provider) siteEmployeesViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_site_revenue_SiteRevenueViewModel, ((Provider) siteRevenueViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_site_sessions_SiteSessionsViewModel, ((Provider) siteSessionsViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_site_stations_SiteStationsViewModel, ((Provider) siteStationsViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_auth_splash_SplashViewModel, ((Provider) splashViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_station_details_StationDetailsViewModel, ((Provider) stationDetailsViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_station_discovery_StationDiscoveryViewModel, ((Provider) stationDiscoveryViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_vehicles_VehiclesViewModel, ((Provider) vehiclesViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_wallet_WalletViewModel, ((Provider) walletViewModelProvider)).put(LazyClassKeyProvider.com_spacetime_evcharging_feature_auth_welcome_WelcomeViewModel, ((Provider) welcomeViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_spacetime_evcharging_feature_auth_welcome_WelcomeViewModel = "com.spacetime.evcharging.feature.auth.welcome.WelcomeViewModel";

      static String com_spacetime_evcharging_feature_customer_home_CustomerHomeViewModel = "com.spacetime.evcharging.feature.customer.home.CustomerHomeViewModel";

      static String com_spacetime_evcharging_feature_station_discovery_StationDiscoveryViewModel = "com.spacetime.evcharging.feature.station.discovery.StationDiscoveryViewModel";

      static String com_spacetime_evcharging_feature_auth_onboarding_OnboardingViewModel = "com.spacetime.evcharging.feature.auth.onboarding.OnboardingViewModel";

      static String com_spacetime_evcharging_feature_auth_otp_OtpVerificationViewModel = "com.spacetime.evcharging.feature.auth.otp.OtpVerificationViewModel";

      static String com_spacetime_evcharging_feature_qr_scanner_QrScannerViewModel = "com.spacetime.evcharging.feature.qr.scanner.QrScannerViewModel";

      static String com_spacetime_evcharging_feature_provider_service_ProviderServiceViewModel = "com.spacetime.evcharging.feature.provider.service.ProviderServiceViewModel";

      static String com_spacetime_evcharging_feature_provider_technicians_ProviderTechniciansViewModel = "com.spacetime.evcharging.feature.provider.technicians.ProviderTechniciansViewModel";

      static String com_spacetime_evcharging_feature_site_revenue_SiteRevenueViewModel = "com.spacetime.evcharging.feature.site.revenue.SiteRevenueViewModel";

      static String com_spacetime_evcharging_feature_charging_ChargingViewModel = "com.spacetime.evcharging.feature.charging.ChargingViewModel";

      static String com_spacetime_evcharging_feature_customer_profile_ProfileViewModel = "com.spacetime.evcharging.feature.customer.profile.ProfileViewModel";

      static String com_spacetime_evcharging_feature_wallet_WalletViewModel = "com.spacetime.evcharging.feature.wallet.WalletViewModel";

      static String com_spacetime_evcharging_feature_provider_dashboard_ProviderDashboardViewModel = "com.spacetime.evcharging.feature.provider.dashboard.ProviderDashboardViewModel";

      static String com_spacetime_evcharging_feature_auth_roleselection_RoleSelectionViewModel = "com.spacetime.evcharging.feature.auth.roleselection.RoleSelectionViewModel";

      static String com_spacetime_evcharging_feature_provider_equipment_ProviderEquipmentViewModel = "com.spacetime.evcharging.feature.provider.equipment.ProviderEquipmentViewModel";

      static String com_spacetime_evcharging_feature_vehicles_VehiclesViewModel = "com.spacetime.evcharging.feature.vehicles.VehiclesViewModel";

      static String com_spacetime_evcharging_feature_site_stations_SiteStationsViewModel = "com.spacetime.evcharging.feature.site.stations.SiteStationsViewModel";

      static String com_spacetime_evcharging_feature_site_employees_SiteEmployeesViewModel = "com.spacetime.evcharging.feature.site.employees.SiteEmployeesViewModel";

      static String com_spacetime_evcharging_feature_site_dashboard_SiteAdminDashboardViewModel = "com.spacetime.evcharging.feature.site.dashboard.SiteAdminDashboardViewModel";

      static String com_spacetime_evcharging_feature_site_sessions_SiteSessionsViewModel = "com.spacetime.evcharging.feature.site.sessions.SiteSessionsViewModel";

      static String com_spacetime_evcharging_feature_auth_splash_SplashViewModel = "com.spacetime.evcharging.feature.auth.splash.SplashViewModel";

      static String com_spacetime_evcharging_feature_station_details_StationDetailsViewModel = "com.spacetime.evcharging.feature.station.details.StationDetailsViewModel";

      static String com_spacetime_evcharging_feature_provider_installations_ProviderInstallationsViewModel = "com.spacetime.evcharging.feature.provider.installations.ProviderInstallationsViewModel";

      @KeepFieldType
      WelcomeViewModel com_spacetime_evcharging_feature_auth_welcome_WelcomeViewModel2;

      @KeepFieldType
      CustomerHomeViewModel com_spacetime_evcharging_feature_customer_home_CustomerHomeViewModel2;

      @KeepFieldType
      StationDiscoveryViewModel com_spacetime_evcharging_feature_station_discovery_StationDiscoveryViewModel2;

      @KeepFieldType
      OnboardingViewModel com_spacetime_evcharging_feature_auth_onboarding_OnboardingViewModel2;

      @KeepFieldType
      OtpVerificationViewModel com_spacetime_evcharging_feature_auth_otp_OtpVerificationViewModel2;

      @KeepFieldType
      QrScannerViewModel com_spacetime_evcharging_feature_qr_scanner_QrScannerViewModel2;

      @KeepFieldType
      ProviderServiceViewModel com_spacetime_evcharging_feature_provider_service_ProviderServiceViewModel2;

      @KeepFieldType
      ProviderTechniciansViewModel com_spacetime_evcharging_feature_provider_technicians_ProviderTechniciansViewModel2;

      @KeepFieldType
      SiteRevenueViewModel com_spacetime_evcharging_feature_site_revenue_SiteRevenueViewModel2;

      @KeepFieldType
      ChargingViewModel com_spacetime_evcharging_feature_charging_ChargingViewModel2;

      @KeepFieldType
      ProfileViewModel com_spacetime_evcharging_feature_customer_profile_ProfileViewModel2;

      @KeepFieldType
      WalletViewModel com_spacetime_evcharging_feature_wallet_WalletViewModel2;

      @KeepFieldType
      ProviderDashboardViewModel com_spacetime_evcharging_feature_provider_dashboard_ProviderDashboardViewModel2;

      @KeepFieldType
      RoleSelectionViewModel com_spacetime_evcharging_feature_auth_roleselection_RoleSelectionViewModel2;

      @KeepFieldType
      ProviderEquipmentViewModel com_spacetime_evcharging_feature_provider_equipment_ProviderEquipmentViewModel2;

      @KeepFieldType
      VehiclesViewModel com_spacetime_evcharging_feature_vehicles_VehiclesViewModel2;

      @KeepFieldType
      SiteStationsViewModel com_spacetime_evcharging_feature_site_stations_SiteStationsViewModel2;

      @KeepFieldType
      SiteEmployeesViewModel com_spacetime_evcharging_feature_site_employees_SiteEmployeesViewModel2;

      @KeepFieldType
      SiteAdminDashboardViewModel com_spacetime_evcharging_feature_site_dashboard_SiteAdminDashboardViewModel2;

      @KeepFieldType
      SiteSessionsViewModel com_spacetime_evcharging_feature_site_sessions_SiteSessionsViewModel2;

      @KeepFieldType
      SplashViewModel com_spacetime_evcharging_feature_auth_splash_SplashViewModel2;

      @KeepFieldType
      StationDetailsViewModel com_spacetime_evcharging_feature_station_details_StationDetailsViewModel2;

      @KeepFieldType
      ProviderInstallationsViewModel com_spacetime_evcharging_feature_provider_installations_ProviderInstallationsViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.spacetime.evcharging.feature.charging.ChargingViewModel 
          return (T) new ChargingViewModel(singletonCImpl.fakeChargingRepositoryProvider.get(), singletonCImpl.fakeWalletRepositoryProvider.get(), viewModelCImpl.savedStateHandle);

          case 1: // com.spacetime.evcharging.feature.customer.home.CustomerHomeViewModel 
          return (T) new CustomerHomeViewModel(singletonCImpl.fakeAuthRepositoryProvider.get(), singletonCImpl.fakeWalletRepositoryProvider.get(), singletonCImpl.fakeStationRepositoryProvider.get(), singletonCImpl.fakeChargingRepositoryProvider.get());

          case 2: // com.spacetime.evcharging.feature.auth.onboarding.OnboardingViewModel 
          return (T) new OnboardingViewModel(singletonCImpl.fakeAuthRepositoryProvider.get(), singletonCImpl.userPreferencesImplProvider.get());

          case 3: // com.spacetime.evcharging.feature.auth.otp.OtpVerificationViewModel 
          return (T) new OtpVerificationViewModel(singletonCImpl.fakeAuthRepositoryProvider.get(), viewModelCImpl.savedStateHandle);

          case 4: // com.spacetime.evcharging.feature.customer.profile.ProfileViewModel 
          return (T) new ProfileViewModel(singletonCImpl.fakeAuthRepositoryProvider.get(), singletonCImpl.fakeStationRepositoryProvider.get(), singletonCImpl.userPreferencesImplProvider.get());

          case 5: // com.spacetime.evcharging.feature.provider.dashboard.ProviderDashboardViewModel 
          return (T) new ProviderDashboardViewModel(singletonCImpl.fakeServiceProviderRepositoryProvider.get());

          case 6: // com.spacetime.evcharging.feature.provider.equipment.ProviderEquipmentViewModel 
          return (T) new ProviderEquipmentViewModel(singletonCImpl.fakeServiceProviderRepositoryProvider.get());

          case 7: // com.spacetime.evcharging.feature.provider.installations.ProviderInstallationsViewModel 
          return (T) new ProviderInstallationsViewModel(singletonCImpl.fakeServiceProviderRepositoryProvider.get());

          case 8: // com.spacetime.evcharging.feature.provider.service.ProviderServiceViewModel 
          return (T) new ProviderServiceViewModel(singletonCImpl.fakeServiceProviderRepositoryProvider.get());

          case 9: // com.spacetime.evcharging.feature.provider.technicians.ProviderTechniciansViewModel 
          return (T) new ProviderTechniciansViewModel(singletonCImpl.fakeServiceProviderRepositoryProvider.get());

          case 10: // com.spacetime.evcharging.feature.qr.scanner.QrScannerViewModel 
          return (T) new QrScannerViewModel(singletonCImpl.fakeChargingRepositoryProvider.get());

          case 11: // com.spacetime.evcharging.feature.auth.roleselection.RoleSelectionViewModel 
          return (T) new RoleSelectionViewModel(singletonCImpl.fakeAuthRepositoryProvider.get(), singletonCImpl.userPreferencesImplProvider.get());

          case 12: // com.spacetime.evcharging.feature.site.dashboard.SiteAdminDashboardViewModel 
          return (T) new SiteAdminDashboardViewModel(singletonCImpl.fakeSiteAdminRepositoryProvider.get());

          case 13: // com.spacetime.evcharging.feature.site.employees.SiteEmployeesViewModel 
          return (T) new SiteEmployeesViewModel(singletonCImpl.fakeSiteAdminRepositoryProvider.get());

          case 14: // com.spacetime.evcharging.feature.site.revenue.SiteRevenueViewModel 
          return (T) new SiteRevenueViewModel(singletonCImpl.fakeSiteAdminRepositoryProvider.get());

          case 15: // com.spacetime.evcharging.feature.site.sessions.SiteSessionsViewModel 
          return (T) new SiteSessionsViewModel(singletonCImpl.fakeSiteAdminRepositoryProvider.get());

          case 16: // com.spacetime.evcharging.feature.site.stations.SiteStationsViewModel 
          return (T) new SiteStationsViewModel(singletonCImpl.fakeSiteAdminRepositoryProvider.get());

          case 17: // com.spacetime.evcharging.feature.auth.splash.SplashViewModel 
          return (T) new SplashViewModel(singletonCImpl.fakeAuthRepositoryProvider.get(), singletonCImpl.userPreferencesImplProvider.get());

          case 18: // com.spacetime.evcharging.feature.station.details.StationDetailsViewModel 
          return (T) new StationDetailsViewModel(singletonCImpl.fakeStationRepositoryProvider.get(), viewModelCImpl.savedStateHandle);

          case 19: // com.spacetime.evcharging.feature.station.discovery.StationDiscoveryViewModel 
          return (T) new StationDiscoveryViewModel(singletonCImpl.fakeStationRepositoryProvider.get());

          case 20: // com.spacetime.evcharging.feature.vehicles.VehiclesViewModel 
          return (T) new VehiclesViewModel(singletonCImpl.fakeVehicleRepositoryProvider.get());

          case 21: // com.spacetime.evcharging.feature.wallet.WalletViewModel 
          return (T) new WalletViewModel(singletonCImpl.fakeWalletRepositoryProvider.get());

          case 22: // com.spacetime.evcharging.feature.auth.welcome.WelcomeViewModel 
          return (T) new WelcomeViewModel(singletonCImpl.fakeAuthRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends SpacetimeApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends SpacetimeApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends SpacetimeApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<FakeChargingRepository> fakeChargingRepositoryProvider;

    private Provider<FakeWalletRepository> fakeWalletRepositoryProvider;

    private Provider<TokenManagerImpl> tokenManagerImplProvider;

    private Provider<FakeAuthRepository> fakeAuthRepositoryProvider;

    private Provider<FakeStationRepository> fakeStationRepositoryProvider;

    private Provider<UserPreferencesImpl> userPreferencesImplProvider;

    private Provider<FakeServiceProviderRepository> fakeServiceProviderRepositoryProvider;

    private Provider<FakeSiteAdminRepository> fakeSiteAdminRepositoryProvider;

    private Provider<FakeVehicleRepository> fakeVehicleRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.fakeChargingRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FakeChargingRepository>(singletonCImpl, 0));
      this.fakeWalletRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FakeWalletRepository>(singletonCImpl, 1));
      this.tokenManagerImplProvider = DoubleCheck.provider(new SwitchingProvider<TokenManagerImpl>(singletonCImpl, 3));
      this.fakeAuthRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FakeAuthRepository>(singletonCImpl, 2));
      this.fakeStationRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FakeStationRepository>(singletonCImpl, 4));
      this.userPreferencesImplProvider = DoubleCheck.provider(new SwitchingProvider<UserPreferencesImpl>(singletonCImpl, 5));
      this.fakeServiceProviderRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FakeServiceProviderRepository>(singletonCImpl, 6));
      this.fakeSiteAdminRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FakeSiteAdminRepository>(singletonCImpl, 7));
      this.fakeVehicleRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FakeVehicleRepository>(singletonCImpl, 8));
    }

    @Override
    public void injectSpacetimeApp(SpacetimeApp spacetimeApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.spacetime.evcharging.data.repository.fake.FakeChargingRepository 
          return (T) new FakeChargingRepository();

          case 1: // com.spacetime.evcharging.data.repository.fake.FakeWalletRepository 
          return (T) new FakeWalletRepository();

          case 2: // com.spacetime.evcharging.data.repository.fake.FakeAuthRepository 
          return (T) new FakeAuthRepository(singletonCImpl.tokenManagerImplProvider.get());

          case 3: // com.spacetime.evcharging.core.security.TokenManagerImpl 
          return (T) new TokenManagerImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.spacetime.evcharging.data.repository.fake.FakeStationRepository 
          return (T) new FakeStationRepository();

          case 5: // com.spacetime.evcharging.core.datastore.UserPreferencesImpl 
          return (T) new UserPreferencesImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 6: // com.spacetime.evcharging.data.repository.fake.FakeServiceProviderRepository 
          return (T) new FakeServiceProviderRepository();

          case 7: // com.spacetime.evcharging.data.repository.fake.FakeSiteAdminRepository 
          return (T) new FakeSiteAdminRepository(singletonCImpl.fakeStationRepositoryProvider.get());

          case 8: // com.spacetime.evcharging.data.repository.fake.FakeVehicleRepository 
          return (T) new FakeVehicleRepository();

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
