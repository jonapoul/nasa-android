plugins {
  id("module-android")
}

android {
  namespace = "nasa.apod.single.nav"
}

dependencies {
  api(projects.modules.apod.model)
  api(projects.modules.apod.nav)
  api(libs.voyager.core)
}
