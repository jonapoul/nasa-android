plugins {
  id("module-android")
}

android {
  namespace = "apod.test.prefs"
}

dependencies {
  api(libs.flowpreferences)
  implementation(libs.test.androidx.coreKtx)
}
