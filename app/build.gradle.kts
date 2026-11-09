plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.spacetime.evcharging"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.spacetime.evcharging"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Core modules
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:security"))

    // Data layer
    implementation(project(":data"))

    // Feature modules
    implementation(project(":feature-auth"))
    implementation(project(":feature-customer-home"))
    implementation(project(":feature-station-discovery"))
    implementation(project(":feature-station-details"))
    implementation(project(":feature-qr-scanner"))
    implementation(project(":feature-charging"))
    implementation(project(":feature-wallet"))
    implementation(project(":feature-vehicles"))
    implementation(project(":feature-customer-profile"))
    implementation(project(":feature-site-dashboard"))
    implementation(project(":feature-site-stations"))
    implementation(project(":feature-site-sessions"))
    implementation(project(":feature-site-revenue"))
    implementation(project(":feature-site-employees"))
    implementation(project(":feature-provider-dashboard"))
    implementation(project(":feature-provider-installations"))
    implementation(project(":feature-provider-equipment"))
    implementation(project(":feature-provider-service"))
    implementation(project(":feature-provider-technicians"))
    implementation(project(":feature-provider-warranty"))

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Logging
    implementation(libs.timber)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
