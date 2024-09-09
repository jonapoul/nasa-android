plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.apod.vm.full"
}

dependencies {
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(libs.kotlinx.datetime)
  api(projects.apod.data.repo)
  api(projects.core.http.progress)

  implementation(libs.hilt.core)
  implementation(libs.kotlinx.coroutines)

  testImplementation(testFixtures(projects.core.model))
}