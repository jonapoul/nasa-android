plugins {
  id("module-android")
}

android {
  namespace = "nasa.apod.single.nav"
}

dependencies {
  api(libs.voyager.core)
  api(projects.modules.apod.model)
  api(projects.modules.apod.nav)
}
