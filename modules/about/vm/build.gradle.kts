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
  api(projects.modules.about.data)
  api(projects.modules.core.url)

  implementation(libs.alakazam.kotlin.core)
  implementation(libs.timber)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
