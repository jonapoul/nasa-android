plugins {
  id("nasa.module.android")
}

android {
  namespace = "nasa.apod.grid.nav"
}

dependencies {
  api(libs.voyager.core)
  api(projects.apod.nav)
}
