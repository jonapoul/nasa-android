plugins {
  id("nasa.module.android")
}

android {
  namespace = "nasa.apod.nav"
}

dependencies {
  api(libs.kotlinx.datetime)
  api(libs.voyager.core)
  api(projects.apod.model)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
