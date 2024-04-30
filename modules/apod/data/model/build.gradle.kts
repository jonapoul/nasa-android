plugins {
  id("module-kotlin")
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(libs.kotlinx.serialization.core)
}
