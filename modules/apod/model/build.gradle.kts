plugins {
  alias(libs.plugins.module.kotlin)
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.serialization.core)

  testFixturesApi(projects.apod.model)
}
