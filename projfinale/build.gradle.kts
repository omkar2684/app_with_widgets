plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false  // Firebase plugin
}

buildscript {
    repositories {
        mavenCentral() // Only Maven Central repository here
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.2") // Firebase plugin classpath
        classpath("com.android.tools.build:gradle:8.7.3") // Android Gradle Plugin
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory.asFile) // Avoid deprecated getter
}

