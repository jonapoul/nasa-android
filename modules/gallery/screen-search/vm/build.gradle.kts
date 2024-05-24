plugins {
  id("module-viewmodel")
}

android {
  namespace = "nasa.gallery.search.vm"
}

dependencies {
  api(projects.modules.gallery.data.repo)
  api(projects.modules.core.url)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.immutable)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
