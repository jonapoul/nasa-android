plugins {
  id("module-kotlin")
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.serialization.core)
  api(libs.retrofit.core)

  implementation(libs.alakazam.kotlin.core)
  implementation(libs.javaxInject)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.serialization.json)

  testImplementation(libs.test.okhttp)
  testImplementation(projects.modules.core.http)
  testImplementation(projects.modules.test.http)
}
