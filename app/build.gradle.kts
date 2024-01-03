@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.workoutlog"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.workoutlog"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    implementation(libs.constraintlayout)
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.material:material:1.0.0")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.camera:camera-core:1.1.0-beta01")
    implementation("androidx.camera:camera-camera2:1.1.0-beta01")
    implementation("androidx.camera:camera-lifecycle:1.1.0-beta01")
    implementation("androidx.camera:camera-video:1.1.0-beta01")
    implementation("androidx.camera:camera-view:1.1.0-beta01")
    implementation("androidx.camera:camera-extensions:1.1.0-beta01")

}