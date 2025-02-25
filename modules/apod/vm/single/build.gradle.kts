plugins {
  alias(libs.plugins.module.viewmodel)
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  api(projects.apod.data.repo)
  api(projects.apod.nav)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.lifecycle.viewmodel.core)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
  compileOnly(libs.hilt.core)
  testImplementation(testFixtures(projects.apod.model))
  testImplementation(testFixtures(projects.core.model))
  testImplementation(projects.test.flow)
}
