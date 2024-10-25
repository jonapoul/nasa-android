plugins {
  kotlin("android")
  id("com.android.library")
  alias(libs.plugins.convention.android.library)
  alias(libs.plugins.convention.diagrams)
  alias(libs.plugins.convention.hilt)
  alias(libs.plugins.convention.style)
  id("com.autonomousapps.dependency-analysis")
  id("com.squareup.sort-dependencies")
  alias(libs.plugins.hilt)
}

android {
  namespace = "nasa.test.hilt"
}

dependencies {
  api(libs.androidx.activity.core)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.hilt.android)
  api(libs.hilt.core)
  api(libs.test.androidx.runner)

  implementation(libs.test.hilt)
}
