plugins {
  id("module-viewmodel")
}

android {
  namespace = "apod.single.vm"
}

dependencies {
  api(projects.modules.apod.data.model)
  api(libs.kotlinx.datetime)
  implementation(projects.modules.apod.data.repo)
  implementation(projects.modules.core.model)
  implementation(libs.alakazam.kotlin.core)
  implementation(libs.alakazam.kotlin.time)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
