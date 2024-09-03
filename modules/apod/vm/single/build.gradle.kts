plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.apod.vm.single"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(libs.kotlinx.datetime)
  api(projects.apod.data.repo)
  api(projects.core.http)

  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.hilt.core)
  implementation(libs.kotlinx.coroutines)

  compileOnly(libs.alakazam.kotlin.compose.annotations)

  testImplementation(testFixtures(projects.apod.model))
  testImplementation(testFixtures(projects.core.model))
}
