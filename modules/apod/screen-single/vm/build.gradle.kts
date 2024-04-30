plugins {
  id("module-viewmodel")
}

android {
  namespace = "apod.single.vm"
}

dependencies {
  api(projects.modules.apod.data.model)
  api(libs.kotlinx.datetime)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
