plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.car"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.car"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation (libs.zxing.android.embedded)
    testImplementation(libs.junit)
    implementation(libs.viewpager)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}