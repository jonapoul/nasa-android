plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.gallery.vm.image"
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(projects.gallery.data.repo)

  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.hilt.android)
  implementation(libs.hilt.core)
  implementation(libs.kotlinx.coroutines)
  implementation(projects.core.http)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
