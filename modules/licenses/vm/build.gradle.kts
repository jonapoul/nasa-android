plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.licenses.vm"
}

dependencies {
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.immutable)
  api(projects.core.url)
  api(projects.licenses.data)

  implementation(libs.alakazam.kotlin.core)
  implementation(libs.timber)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
