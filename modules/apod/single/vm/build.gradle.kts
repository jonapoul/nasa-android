plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.apod.single.vm"
}

dependencies {
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  api(projects.apod.data.repo)
  api(projects.apod.nav)
  api(projects.core.http)
  api(projects.core.url)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
