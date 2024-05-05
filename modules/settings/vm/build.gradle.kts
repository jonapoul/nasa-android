plugins {
  id("module-viewmodel")
}

android {
  namespace = "apod.settings.vm"
}

dependencies {
  api(projects.modules.core.url)
  api(projects.modules.apod.data.repo)
  implementation(libs.alakazam.android.core)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
