plugins {
  id("module-android")
}

android {
  namespace = "nasa.apod.grid.nav"
}

dependencies {
  api(projects.modules.apod.nav)
  api(libs.voyager.core)
}
