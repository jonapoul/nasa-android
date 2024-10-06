plugins {
  id("nasa.module.kotlin")
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.javaxInject)
  api(libs.okhttp.core)
  api(projects.core.model)

  implementation(libs.kotlinx.coroutines)
  implementation(libs.okio)
}
