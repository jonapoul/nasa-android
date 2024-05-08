plugins {
  id("module-viewmodel")
}

android {
  namespace = "nasa.gallery.vm"
}

dependencies {
  api(projects.modules.gallery.data.repo)
  api(projects.modules.gallery.model)
  api(projects.modules.core.url)
  api(projects.modules.nav.args)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.immutable)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
