import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("android")
  id("com.android.library")
  id("convention-compose")
  id("convention-style")
  id("com.squareup.sort-dependencies")
  id("com.dropbox.dependency-guard")
}

android {
  buildFeatures {
    androidResources = true
    resValues = true
  }
}

dependencyGuard {
  configuration("releaseRuntimeClasspath")
}

tasks.withType<KotlinCompile> {
  compilerOptions {
    // Allow M3 experimental APIs - most of them are experimental anyway
    freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
  }
}
