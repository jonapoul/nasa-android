plugins {
  id("module-viewmodel")
}

android {
  namespace = "apod.settings.vm"
}

dependencies {
  api(projects.modules.core.url)
  implementation(projects.modules.core.model)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
