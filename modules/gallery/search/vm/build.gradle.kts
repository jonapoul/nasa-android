plugins {
  id("module-viewmodel")
}

android {
  namespace = "nasa.gallery.search.vm"
}

dependencies {
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.immutable)
  api(projects.modules.core.url)
  api(projects.modules.gallery.data.repo)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
