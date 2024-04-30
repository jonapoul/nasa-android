plugins {
  id("module-compose")
}

android {
  namespace = "apod.navigation"
}

dependencies {
  api(libs.voyager.core)
  implementation(libs.androidx.compose.runtime)
}
