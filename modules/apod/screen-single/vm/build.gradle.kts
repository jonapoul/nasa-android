plugins {
  id("module-viewmodel")
}

android {
  namespace = "apod.single.vm"
}

dependencies {
  api(projects.modules.apod.data.repo)
  api(projects.modules.core.url)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
