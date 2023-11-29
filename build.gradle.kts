// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.5")
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        classpath("org.jacoco:org.jacoco.core:0.8.10")
    }
}

plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.android.library") version "8.1.2" apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
    id("io.gitlab.arturbosch.detekt") version ("1.23.3") apply false
}