plugins {
  id("module-android")
}

android {
  namespace = "apod.core.url"
}

dependencies {
  api(libs.javaxInject)
  implementation(libs.androidx.core)
}
