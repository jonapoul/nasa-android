plugins {
  id("module-android")
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "apod.data.api"
}

dependencies {
  api(projects.modules.apod.data.model)
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.serialization.core)
  api(libs.retrofit.core)
  implementation(projects.modules.core.model)
  implementation(libs.kotlinx.serialization.json)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
  testImplementation(projects.modules.core.http)
  testImplementation(projects.modules.test.http)
  testImplementation(libs.okhttp.core)
  testImplementation(libs.test.okhttp)
}
