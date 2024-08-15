plugins {
  id("nasa.module.kotlin")
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.javaxInject)
  api(libs.kotlinx.serialization.json)
  api(libs.okhttp.core)
  api(libs.retrofit.core)
  api(projects.modules.core.model)

  implementation(libs.kotlinx.serialization.core)
  implementation(libs.okhttp.logging)
  implementation(libs.okio)
  implementation(libs.retrofit.serialization)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
