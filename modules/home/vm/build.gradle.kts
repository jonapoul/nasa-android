plugins {
  alias(libs.plugins.module.viewmodel)
}

android {
  namespace = "nasa.home.vm"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  api(projects.apod.data.repo)
  api(projects.core.http.usage)
  api(projects.gallery.data.repo)

  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.hilt.core)

  compileOnly(libs.alakazam.kotlin.compose.annotations)

  testImplementation(testFixtures(projects.apod.model))
  testImplementation(projects.test.flow)
  testImplementation(projects.test.http)
  testImplementation(projects.test.prefs)
}
