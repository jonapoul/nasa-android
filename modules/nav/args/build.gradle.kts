plugins {
  id("module-android")
}

android {
  namespace = "nasa.nav.args"
}

dependencies {
  api(libs.kotlinx.datetime)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
