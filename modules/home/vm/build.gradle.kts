plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.home.vm"
}

dependencies {
  api(libs.kotlinx.coroutines)
  api(projects.core.http)
  api(projects.core.url)

  implementation(libs.javaxInject)
  implementation(projects.apod.data.repo)
  implementation(projects.gallery.data.repo)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
