plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.apod.vm.grid"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(libs.kotlinx.datetime)
  api(projects.apod.data.repo)

  implementation(libs.hilt.core)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.immutable)

  compileOnly(libs.alakazam.kotlin.compose.annotations)

  testImplementation(testFixtures(projects.apod.model))
  testImplementation(testFixtures(projects.core.model))
}
