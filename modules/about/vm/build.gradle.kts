plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.about.vm"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.kotlinx.coroutines)
  api(projects.about.data)

  implementation(libs.hilt.core)
  implementation(libs.kotlinx.datetime)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
