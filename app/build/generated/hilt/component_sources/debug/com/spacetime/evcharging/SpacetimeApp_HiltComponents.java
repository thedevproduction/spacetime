package com.spacetime.evcharging;

import com.spacetime.evcharging.core.datastore.di.DataStoreModule;
import com.spacetime.evcharging.core.network.di.NetworkModule;
import com.spacetime.evcharging.core.security.di.SecurityModule;
import com.spacetime.evcharging.data.di.DataModule;
import com.spacetime.evcharging.feature.auth.onboarding.OnboardingViewModel_HiltModules;
import com.spacetime.evcharging.feature.auth.otp.OtpVerificationViewModel_HiltModules;
import com.spacetime.evcharging.feature.auth.roleselection.RoleSelectionViewModel_HiltModules;
import com.spacetime.evcharging.feature.auth.splash.SplashViewModel_HiltModules;
import com.spacetime.evcharging.feature.auth.welcome.WelcomeViewModel_HiltModules;
import com.spacetime.evcharging.feature.charging.ChargingViewModel_HiltModules;
import com.spacetime.evcharging.feature.customer.home.CustomerHomeViewModel_HiltModules;
import com.spacetime.evcharging.feature.customer.profile.ProfileViewModel_HiltModules;
import com.spacetime.evcharging.feature.provider.dashboard.ProviderDashboardViewModel_HiltModules;
import com.spacetime.evcharging.feature.provider.equipment.ProviderEquipmentViewModel_HiltModules;
import com.spacetime.evcharging.feature.provider.installations.ProviderInstallationsViewModel_HiltModules;
import com.spacetime.evcharging.feature.provider.service.ProviderServiceViewModel_HiltModules;
import com.spacetime.evcharging.feature.provider.technicians.ProviderTechniciansViewModel_HiltModules;
import com.spacetime.evcharging.feature.qr.scanner.QrScannerViewModel_HiltModules;
import com.spacetime.evcharging.feature.site.dashboard.SiteAdminDashboardViewModel_HiltModules;
import com.spacetime.evcharging.feature.site.employees.SiteEmployeesViewModel_HiltModules;
import com.spacetime.evcharging.feature.site.revenue.SiteRevenueViewModel_HiltModules;
import com.spacetime.evcharging.feature.site.sessions.SiteSessionsViewModel_HiltModules;
import com.spacetime.evcharging.feature.site.stations.SiteStationsViewModel_HiltModules;
import com.spacetime.evcharging.feature.station.details.StationDetailsViewModel_HiltModules;
import com.spacetime.evcharging.feature.station.discovery.StationDiscoveryViewModel_HiltModules;
import com.spacetime.evcharging.feature.vehicles.VehiclesViewModel_HiltModules;
import com.spacetime.evcharging.feature.wallet.WalletViewModel_HiltModules;
import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Subcomponent;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.components.ViewComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.components.ViewWithFragmentComponent;
import dagger.hilt.android.flags.FragmentGetContextFix;
import dagger.hilt.android.flags.HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_DefaultViewModelFactories_ActivityModule;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ViewModelModule;
import dagger.hilt.android.internal.managers.ActivityComponentManager;
import dagger.hilt.android.internal.managers.FragmentComponentManager;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_LifecycleModule;
import dagger.hilt.android.internal.managers.HiltWrapper_SavedStateHandleModule;
import dagger.hilt.android.internal.managers.ServiceComponentManager;
import dagger.hilt.android.internal.managers.ViewComponentManager;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.HiltWrapper_ActivityModule;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.android.scopes.FragmentScoped;
import dagger.hilt.android.scopes.ServiceScoped;
import dagger.hilt.android.scopes.ViewModelScoped;
import dagger.hilt.android.scopes.ViewScoped;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedComponent;
import dagger.hilt.migration.DisableInstallInCheck;
import javax.annotation.processing.Generated;
import javax.inject.Singleton;

@Generated("dagger.hilt.processor.internal.root.RootProcessor")
public final class SpacetimeApp_HiltComponents {
  private SpacetimeApp_HiltComponents() {
  }

  @Module(
      subcomponents = ServiceC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ServiceCBuilderModule {
    @Binds
    ServiceComponentBuilder bind(ServiceC.Builder builder);
  }

  @Module(
      subcomponents = ActivityRetainedC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ActivityRetainedCBuilderModule {
    @Binds
    ActivityRetainedComponentBuilder bind(ActivityRetainedC.Builder builder);
  }

  @Module(
      subcomponents = ActivityC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ActivityCBuilderModule {
    @Binds
    ActivityComponentBuilder bind(ActivityC.Builder builder);
  }

  @Module(
      subcomponents = ViewModelC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewModelCBuilderModule {
    @Binds
    ViewModelComponentBuilder bind(ViewModelC.Builder builder);
  }

  @Module(
      subcomponents = ViewC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewCBuilderModule {
    @Binds
    ViewComponentBuilder bind(ViewC.Builder builder);
  }

  @Module(
      subcomponents = FragmentC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface FragmentCBuilderModule {
    @Binds
    FragmentComponentBuilder bind(FragmentC.Builder builder);
  }

  @Module(
      subcomponents = ViewWithFragmentC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewWithFragmentCBuilderModule {
    @Binds
    ViewWithFragmentComponentBuilder bind(ViewWithFragmentC.Builder builder);
  }

  @Component(
      modules = {
          ApplicationContextModule.class,
          DataModule.class,
          DataStoreModule.class,
          HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule.class,
          NetworkModule.class,
          SecurityModule.class,
          ActivityRetainedCBuilderModule.class,
          ServiceCBuilderModule.class
      }
  )
  @Singleton
  public abstract static class SingletonC implements SpacetimeApp_GeneratedInjector,
      FragmentGetContextFix.FragmentGetContextFixEntryPoint,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint,
      ServiceComponentManager.ServiceComponentBuilderEntryPoint,
      SingletonComponent,
      GeneratedComponent {
  }

  @Subcomponent
  @ServiceScoped
  public abstract static class ServiceC implements ServiceComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ServiceComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          ChargingViewModel_HiltModules.KeyModule.class,
          CustomerHomeViewModel_HiltModules.KeyModule.class,
          HiltWrapper_ActivityRetainedComponentManager_LifecycleModule.class,
          HiltWrapper_SavedStateHandleModule.class,
          OnboardingViewModel_HiltModules.KeyModule.class,
          OtpVerificationViewModel_HiltModules.KeyModule.class,
          ProfileViewModel_HiltModules.KeyModule.class,
          ProviderDashboardViewModel_HiltModules.KeyModule.class,
          ProviderEquipmentViewModel_HiltModules.KeyModule.class,
          ProviderInstallationsViewModel_HiltModules.KeyModule.class,
          ProviderServiceViewModel_HiltModules.KeyModule.class,
          ProviderTechniciansViewModel_HiltModules.KeyModule.class,
          QrScannerViewModel_HiltModules.KeyModule.class,
          RoleSelectionViewModel_HiltModules.KeyModule.class,
          SiteAdminDashboardViewModel_HiltModules.KeyModule.class,
          SiteEmployeesViewModel_HiltModules.KeyModule.class,
          SiteRevenueViewModel_HiltModules.KeyModule.class,
          SiteSessionsViewModel_HiltModules.KeyModule.class,
          SiteStationsViewModel_HiltModules.KeyModule.class,
          ActivityCBuilderModule.class,
          ViewModelCBuilderModule.class,
          SplashViewModel_HiltModules.KeyModule.class,
          StationDetailsViewModel_HiltModules.KeyModule.class,
          StationDiscoveryViewModel_HiltModules.KeyModule.class,
          VehiclesViewModel_HiltModules.KeyModule.class,
          WalletViewModel_HiltModules.KeyModule.class,
          WelcomeViewModel_HiltModules.KeyModule.class
      }
  )
  @ActivityRetainedScoped
  public abstract static class ActivityRetainedC implements ActivityRetainedComponent,
      ActivityComponentManager.ActivityComponentBuilderEntryPoint,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityRetainedComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          HiltWrapper_ActivityModule.class,
          HiltWrapper_DefaultViewModelFactories_ActivityModule.class,
          FragmentCBuilderModule.class,
          ViewCBuilderModule.class
      }
  )
  @ActivityScoped
  public abstract static class ActivityC implements MainActivity_GeneratedInjector,
      ActivityComponent,
      DefaultViewModelFactories.ActivityEntryPoint,
      HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint,
      FragmentComponentManager.FragmentComponentBuilderEntryPoint,
      ViewComponentManager.ViewComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          ChargingViewModel_HiltModules.BindsModule.class,
          CustomerHomeViewModel_HiltModules.BindsModule.class,
          HiltWrapper_HiltViewModelFactory_ViewModelModule.class,
          OnboardingViewModel_HiltModules.BindsModule.class,
          OtpVerificationViewModel_HiltModules.BindsModule.class,
          ProfileViewModel_HiltModules.BindsModule.class,
          ProviderDashboardViewModel_HiltModules.BindsModule.class,
          ProviderEquipmentViewModel_HiltModules.BindsModule.class,
          ProviderInstallationsViewModel_HiltModules.BindsModule.class,
          ProviderServiceViewModel_HiltModules.BindsModule.class,
          ProviderTechniciansViewModel_HiltModules.BindsModule.class,
          QrScannerViewModel_HiltModules.BindsModule.class,
          RoleSelectionViewModel_HiltModules.BindsModule.class,
          SiteAdminDashboardViewModel_HiltModules.BindsModule.class,
          SiteEmployeesViewModel_HiltModules.BindsModule.class,
          SiteRevenueViewModel_HiltModules.BindsModule.class,
          SiteSessionsViewModel_HiltModules.BindsModule.class,
          SiteStationsViewModel_HiltModules.BindsModule.class,
          SplashViewModel_HiltModules.BindsModule.class,
          StationDetailsViewModel_HiltModules.BindsModule.class,
          StationDiscoveryViewModel_HiltModules.BindsModule.class,
          VehiclesViewModel_HiltModules.BindsModule.class,
          WalletViewModel_HiltModules.BindsModule.class,
          WelcomeViewModel_HiltModules.BindsModule.class
      }
  )
  @ViewModelScoped
  public abstract static class ViewModelC implements ViewModelComponent,
      HiltViewModelFactory.ViewModelFactoriesEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewModelComponentBuilder {
    }
  }

  @Subcomponent
  @ViewScoped
  public abstract static class ViewC implements ViewComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewComponentBuilder {
    }
  }

  @Subcomponent(
      modules = ViewWithFragmentCBuilderModule.class
  )
  @FragmentScoped
  public abstract static class FragmentC implements FragmentComponent,
      DefaultViewModelFactories.FragmentEntryPoint,
      ViewComponentManager.ViewWithFragmentComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends FragmentComponentBuilder {
    }
  }

  @Subcomponent
  @ViewScoped
  public abstract static class ViewWithFragmentC implements ViewWithFragmentComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewWithFragmentComponentBuilder {
    }
  }
}
