plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.about.vm"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  api(projects.about.data)
  api(projects.core.url)

  implementation(libs.alakazam.kotlin.core)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
