plugins {
  id("module-compose")
}

android {
  namespace = "apod.navigation"
}

dependencies {
  api(projects.modules.core.model)
  api(libs.kotlinx.datetime)
  api(libs.voyager.core)
  implementation(libs.androidx.compose.runtime)
}
