plugins {
  id("module-kotlin")
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
