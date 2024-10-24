plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.licenses.vm"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  api(projects.licenses.data)

  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.hilt.core)
  implementation(libs.kotlinx.immutable)

  compileOnly(libs.alakazam.kotlin.compose.annotations)

  testImplementation(projects.test.flow)
}
