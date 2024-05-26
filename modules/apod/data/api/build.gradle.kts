plugins {
  id("module-kotlin")
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(projects.modules.apod.model)
  api(projects.modules.core.model)
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.serialization.core)
  api(libs.kotlinx.serialization.json)
  api(libs.retrofit.core)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
  testImplementation(projects.modules.core.http)
  testImplementation(projects.modules.test.http)
  testImplementation(libs.test.okhttp)
}
