plugins {
  id("nasa.module.android")
  id("nasa.convention.hilt")
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
  api(libs.test.hilt)

  implementation(libs.androidx.annotation)
}
