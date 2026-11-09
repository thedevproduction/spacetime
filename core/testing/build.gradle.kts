plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.spacetime.evcharging.core.testing"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Module dependencies
    implementation(project(":core:common"))

    // Testing
    implementation(libs.junit)
    implementation(libs.mockk)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.turbine)
}
