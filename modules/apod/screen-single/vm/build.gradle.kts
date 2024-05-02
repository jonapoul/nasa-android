plugins {
  id("module-viewmodel")
}

android {
  namespace = "apod.single.vm"
}

dependencies {
  api(projects.modules.apod.data.model)
  api(libs.alakazam.kotlin.time)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  implementation(projects.modules.apod.data.repo)
  implementation(projects.modules.core.model)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
