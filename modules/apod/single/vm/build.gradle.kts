plugins {
  id("module-viewmodel")
}

android {
  namespace = "nasa.apod.single.vm"
}

dependencies {
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  api(projects.modules.apod.data.repo)
  api(projects.modules.apod.nav)
  api(projects.modules.core.http)
  api(projects.modules.core.url)

  implementation(libs.timber)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
