plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.home.vm"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(projects.apod.data.repo)
  api(projects.core.http.usage)
  api(projects.gallery.data.repo)

  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.hilt.core)
  implementation(libs.kotlinx.coroutines)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
