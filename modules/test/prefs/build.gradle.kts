plugins {
  id("module-android")
}

android {
  namespace = "nasa.test.prefs"
}

dependencies {
  api(libs.flowpreferences)
  implementation(libs.test.androidx.coreKtx)
}
