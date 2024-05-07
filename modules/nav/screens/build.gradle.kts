plugins {
  id("module-compose")
}

android {
  namespace = "nasa.nav.screens"
}

dependencies {
  api(projects.modules.apod.model)
  api(projects.modules.nav.args)
  api(libs.voyager.core)
  implementation(libs.androidx.compose.runtime)
}
