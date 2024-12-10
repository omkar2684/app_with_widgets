plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services") // Firebase plugin
}

android {
    namespace = "com.example.despuapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.despuapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.6.0")) // Firebase BOM for version alignment
    implementation("com.google.firebase:firebase-database") // Firebase Realtime Database
    implementation("com.google.firebase:firebase-auth") // Firebase Authentication (if needed)

    // WorkManager dependencies
    implementation("androidx.work:work-runtime-ktx:2.10.0") // WorkManager with Kotlin extensions

    // AndroidX libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose dependencies
    implementation(platform(libs.androidx.compose.bom)) // BOM for Compose
    implementation(libs.androidx.ui) // Compose UI
    implementation(libs.androidx.ui.graphics) // Compose graphics
    implementation(libs.androidx.ui.tooling.preview) // Compose tooling preview
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material:material:1.6.0")

    implementation("androidx.compose.material3:material3:1.2.0") // Replace with the latest version
    // Material3 for Compose
    implementation(libs.androidx.constraintlayout) // ConstraintLayout for Compose

    // RecyclerView (if used)
    implementation("androidx.recyclerview:recyclerview:1.3.1")

    // Testing dependencies
    testImplementation(libs.junit) // JUnit for testing
    androidTestImplementation(libs.androidx.junit) // AndroidX JUnit for testing
    androidTestImplementation(libs.androidx.espresso.core) // Espresso for UI testing
    androidTestImplementation(platform(libs.androidx.compose.bom)) // Compose BOM for testing
    androidTestImplementation(libs.androidx.ui.test.junit4) // Compose UI testing
    debugImplementation(libs.androidx.ui.tooling) // Compose tooling for debugging
    debugImplementation(libs.androidx.ui.test.manifest) // Compose testing manifest
}

// Apply the Firebase plugin
apply(plugin = "com.google.gms.google-services")
