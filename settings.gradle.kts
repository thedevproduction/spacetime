pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SpacetimeEnergyFutures"

// App module
include(":app")

// Core modules
include(":core:designsystem")
include(":core:common")
include(":core:navigation")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:security")
include(":core:testing")

// Data layer
include(":data")

// Customer feature modules
include(":feature-auth")
include(":feature-customer-home")
include(":feature-station-discovery")
include(":feature-station-details")
include(":feature-qr-scanner")
include(":feature-charging")
include(":feature-wallet")
include(":feature-vehicles")
include(":feature-customer-profile")

// Site Admin feature modules
include(":feature-site-dashboard")
include(":feature-site-stations")
include(":feature-site-sessions")
include(":feature-site-revenue")
include(":feature-site-employees")

// Service Provider feature modules
include(":feature-provider-dashboard")
include(":feature-provider-installations")
include(":feature-provider-equipment")
include(":feature-provider-service")
include(":feature-provider-technicians")
include(":feature-provider-warranty")

