plugins {
  id("module-android")
}

android {
  namespace = "nasa.test.prefs"
}

dependencies {
  api(libs.preferences.android)
  api(libs.preferences.core)

  implementation(libs.test.androidx.coreKtx)
}
