plugins {
  id("nasa.module.kotlin")
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.serialization.core)

  testFixturesApi(projects.apod.model)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
