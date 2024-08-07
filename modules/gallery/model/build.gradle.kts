plugins {
  id("nasa.module.kotlin")
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(libs.kotlinx.immutable)
  api(libs.kotlinx.serialization.core)
  api(libs.kotlinx.serialization.json)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
