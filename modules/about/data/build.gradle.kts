plugins {
  id("module-kotlin")
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.serialization.core)
  api(libs.alakazam.kotlin.core)
  api(libs.javaxInject)
  api(libs.retrofit.core)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.serialization.json)
  testImplementation(projects.modules.core.http)
  testImplementation(projects.modules.test.http)
  testImplementation(libs.okhttp.core)
  testImplementation(libs.test.okhttp)
}
