plugins {
  id("module-kotlin")
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.javaxInject)
  api(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.serialization.json)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
