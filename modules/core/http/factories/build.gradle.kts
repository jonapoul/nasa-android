plugins {
  alias(libs.plugins.module.kotlin)
}

dependencies {
  api(libs.kotlinx.serialization.json)
  api(libs.okhttp.core)
  api(libs.retrofit.core)

  implementation(libs.kotlinx.serialization.core)
  implementation(libs.okhttp.logging)
  implementation(libs.retrofit.serialization)
}
