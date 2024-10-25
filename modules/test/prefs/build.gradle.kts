plugins {
  alias(libs.plugins.module.android)
}

android {
  namespace = "nasa.test.prefs"
}

dependencies {
  api(libs.preferences.core)

  implementation(libs.preferences.android)
  implementation(libs.test.androidx.coreKtx)
}
