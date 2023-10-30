// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies{
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN_VERSION}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAV_SAFE_ARGS_VERSION}")
    }
}

plugins {
    id("com.android.application") version "8.1.1" apply false
    id("com.google.devtools.ksp") version "1.8.0-1.0.9" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
}