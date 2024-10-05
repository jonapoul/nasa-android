plugins {
  id("nasa.module.kotlin")
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)

  testFixturesApi(projects.core.model)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
