plugins {
  id("module-compose")
}

android {
  namespace = "apod.nav.screens"
}

dependencies {
  api(projects.modules.nav.args)
  api(projects.modules.core.model)
  api(libs.voyager.core)
  implementation(libs.androidx.compose.runtime)
}
