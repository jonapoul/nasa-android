plugins {
  id("module-android")
}

android {
  namespace = "nasa.apod.nav"
}

dependencies {
  api(libs.kotlinx.datetime)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
