plugins {
  id("module-android")
}

android {
  namespace = "nasa.core.url"
}

dependencies {
  api(libs.javaxInject)
  implementation(libs.androidx.core)
}
