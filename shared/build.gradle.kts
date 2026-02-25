plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
    id("com.android.library")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
                implementation("io.insert-koin:koin-core:4.0.0")
                implementation("io.insert-koin:koin-compose:4.0.0")
                implementation("io.insert-koin:koin-compose-viewmodel:4.0.0")
                implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")
                implementation("dev.gitlive:firebase-auth:2.1.0")
                implementation("dev.gitlive:firebase-firestore:2.1.0")
                implementation("dev.gitlive:firebase-database:2.1.0")
                implementation("androidx.room:room-runtime:2.7.0")
                implementation("androidx.sqlite:sqlite-bundled:2.5.0")
                implementation("io.coil-kt.coil3:coil-compose:3.0.4")
                implementation("io.coil-kt.coil3:coil-network-ktor3:3.0.4")
                implementation("io.ktor:ktor-client-core:3.0.3")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.insert-koin:koin-android:4.0.0")
                implementation("io.insert-koin:koin-androidx-compose:4.0.0")
                implementation("io.ktor:ktor-client-okhttp:3.0.3")
                implementation("com.google.android.gms:play-services-maps:18.2.0")
                implementation("com.google.android.gms:play-services-ads:22.5.0")
                implementation("androidx.webkit:webkit:1.12.1")
            }
        }
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:3.0.3")
            }
        }
        val iosX64Main by getting { dependsOn(iosMain) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
    }
}

android {
    namespace = "com.suchelin.shared"
    compileSdk = 35

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
