plugins {
  alias(libs.plugins.module.viewmodel)
}

dependencies {
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  api(projects.apod.data.repo)
  api(projects.core.http.progress)

  implementation(libs.androidx.compose.runtime)
  implementation(libs.hilt.core)

  testImplementation(testFixtures(projects.core.model))
  testImplementation(projects.test.flow)
}
