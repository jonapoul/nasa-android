plugins {
  id("module-kotlin")
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.javaxInject)
  api(libs.kotlinx.serialization.json)
  api(libs.okhttp.core)
  api(libs.retrofit.core)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.okhttp.logging)
  implementation(libs.okio)
  implementation(libs.retrofit.serialization)
}
