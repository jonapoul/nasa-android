plugins {
  id("convention-android-library")
  id("convention-licensee")
  id("convention-spotless")
  id("com.squareup.sort-dependencies")
  id("com.dropbox.dependency-guard")
}

dependencyGuard {
  configuration("releaseRuntimeClasspath")
}

android {
  buildFeatures {
    androidResources = true
    resValues = true
  }
}
