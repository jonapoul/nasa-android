plugins {
  id("nasa.module.android")
}

android {
  namespace = "nasa.apod.single.nav"
}

dependencies {
  api(libs.voyager.core)
  api(projects.apod.model)
  api(projects.apod.nav)
}
