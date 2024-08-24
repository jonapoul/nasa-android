plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.gallery.search.vm"
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.immutable)
  api(projects.gallery.data.repo)

  implementation(libs.timber)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
