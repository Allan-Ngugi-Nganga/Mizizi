// Top-level build file. Plugins are declared here once and applied in :app.
plugins {
    alias(libs.plugins.android.application) apply false
    // Safe Args: generates type-safe navigation directions and arguments
    id("androidx.navigation.safeargs.kotlin") version "2.9.8" apply false
    // KSP: generates Room's database code at compile time.
    // 2.3.x is the standalone KSP line that works with AGP 9's built-in Kotlin.
    id("com.google.devtools.ksp") version "2.3.14" apply false
}
