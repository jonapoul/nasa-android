plugins {
  id("module-viewmodel")
}

android {
  namespace = "apod.single.vm"
}

dependencies {
  api(projects.modules.core.model)
  api(projects.modules.core.url)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  implementation(projects.modules.apod.data.repo)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
