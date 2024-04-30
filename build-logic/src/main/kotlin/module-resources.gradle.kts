@file:Suppress("UnstableApiUsage")

plugins {
  id("com.android.library")
  id("convention-android-base")
  id("convention-licensee")
  id("convention-spotless")
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
