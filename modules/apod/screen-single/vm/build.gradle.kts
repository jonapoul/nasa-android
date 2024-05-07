plugins {
  id("module-viewmodel")
}

android {
  namespace = "nasa.apod.single.vm"
}

dependencies {
  api(projects.modules.apod.data.repo)
  api(projects.modules.apod.model)
  api(projects.modules.core.http)
  api(projects.modules.core.url)
  api(projects.modules.nav.args)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
