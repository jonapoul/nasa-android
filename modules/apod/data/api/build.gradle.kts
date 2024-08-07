plugins {
  id("nasa.module.kotlin")
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.serialization.core)
  api(libs.kotlinx.serialization.json)
  api(libs.retrofit.core)
  api(projects.modules.apod.model)
  api(projects.modules.core.model)

  compileOnly(libs.alakazam.kotlin.compose.annotations)

  testImplementation(libs.test.okhttp)
  testImplementation(projects.modules.core.http)
  testImplementation(projects.modules.test.http)
}
