plugins {
  alias(libs.plugins.module.kotlin)
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.javaxInject)
  api(libs.okhttp.core)
  api(projects.core.model)

  implementation(libs.kotlinx.coroutines)
  implementation(libs.okio)

  testImplementation(libs.retrofit.core)
  testImplementation(libs.retrofit.scalars)
  testImplementation(libs.test.okhttp)
  testImplementation(projects.core.http.factories)
  testImplementation(projects.test.flow)
  testImplementation(projects.test.http)
}
