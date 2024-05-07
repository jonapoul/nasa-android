plugins {
  id("module-viewmodel")
}

android {
  namespace = "nasa.about.vm"
}

dependencies {
  api(projects.modules.about.data)
  api(projects.modules.core.url)
  api(libs.alakazam.android.core)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  implementation(libs.alakazam.kotlin.core)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
