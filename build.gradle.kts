// Top-level build.gradle.kts (project root)

plugins {
    id("com.android.application") version "8.4.0" apply false
    id("com.android.library") version "8.4.0" apply false
    kotlin("android") version "1.9.24" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false // ✅ Safe Args
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
