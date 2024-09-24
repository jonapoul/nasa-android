plugins {
  kotlin("android")
  id("com.android.library")
  id("nasa.convention.android.library")
  id("nasa.convention.diagrams")
  id("nasa.convention.hilt")
  id("nasa.convention.style")
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
