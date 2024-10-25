plugins {
  alias(libs.plugins.module.navigation)
}

dependencies {
  api(libs.kotlinx.datetime)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
