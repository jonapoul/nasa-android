plugins {
  id("nasa.module.kotlin")
  id("nasa.convention.coroutines")
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.javaxInject)
  api(libs.okhttp.core)

  implementation(libs.kotlinx.coroutines)

  testImplementation(libs.kotlinx.serialization.core)
  testImplementation(libs.kotlinx.serialization.json)
  testImplementation(libs.retrofit.core)
  testImplementation(libs.test.okhttp)
  testImplementation(projects.core.http.factories)
  testImplementation(projects.test.flow)
  testImplementation(projects.test.http)
}
