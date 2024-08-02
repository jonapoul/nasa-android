plugins {
  kotlin("android")
  id("convention-android-library")
  id("convention-hilt")
  id("convention-style")
  id("convention-test")
  id("com.squareup.sort-dependencies")
  id("com.dropbox.dependency-guard")
}

val api by configurations
val implementation by configurations

dependencies {
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.dagger.core)
  api(libs.javaxInject)
  implementation(libs.hilt.android)
}

dependencyGuard {
  configuration("releaseRuntimeClasspath")
}
