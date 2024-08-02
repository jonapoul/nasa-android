plugins {
  id("module-android")
}

android {
  namespace = "nasa.apod.grid.nav"
}

dependencies {
  api(libs.voyager.core)
  api(projects.modules.apod.nav)
}
