plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.gallery.vm.search"
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  api(projects.gallery.data.repo)

  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.hilt.core)
  implementation(libs.kotlinx.immutable)
  implementation(libs.preferences.core)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
