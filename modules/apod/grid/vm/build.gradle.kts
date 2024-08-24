plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.apod.grid.vm"
}

dependencies {
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.immutable)
  api(projects.apod.data.repo)
  api(projects.apod.nav)
  api(projects.core.url)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
