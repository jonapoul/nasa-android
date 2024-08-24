plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.gallery.image.vm"
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.kotlinx.coroutines)
  api(projects.core.model)
  api(projects.gallery.data.repo)

  implementation(libs.timber)
  implementation(projects.core.http)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
