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
  api(projects.core.http.usage)

  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.hilt.core)
  implementation(libs.javaxInject)
  implementation(libs.kotlinx.coroutines)
  implementation(projects.apod.data.repo)
  implementation(projects.gallery.data.repo)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
