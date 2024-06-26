plugins {
  id("module-viewmodel")
}

android {
  namespace = "nasa.licenses.vm"
}

dependencies {
  api(projects.modules.core.url)
  api(projects.modules.licenses.data)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.immutable)
  implementation(libs.alakazam.kotlin.core)
  implementation(libs.timber)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
