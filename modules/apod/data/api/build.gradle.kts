plugins {
  alias(libs.plugins.module.kotlin)
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.serialization.core)
  api(libs.kotlinx.serialization.json)
  api(libs.retrofit.core)
  api(projects.apod.model)
  api(projects.core.model)

  compileOnly(libs.alakazam.kotlin.compose.annotations)

  testImplementation(libs.test.okhttp)
  testImplementation(projects.test.http)
}
