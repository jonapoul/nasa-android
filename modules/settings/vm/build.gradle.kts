plugins {
  id("module-viewmodel")
}

android {
  namespace = "apod.settings.vm"
}

dependencies {
  api(projects.modules.core.url)
  api(projects.modules.apod.data.repo)
  api(libs.alakazam.android.core)
  api(libs.kotlinx.coroutines)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}