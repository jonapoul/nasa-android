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
  api(projects.modules.core.model)
  api(projects.modules.gallery.data.repo)

  implementation(libs.timber)
  implementation(projects.modules.core.http)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
