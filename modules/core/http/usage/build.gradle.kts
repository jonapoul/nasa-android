plugins {
  id("nasa.module.kotlin")
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.javaxInject)
  api(libs.okhttp.core)

  implementation(libs.kotlinx.coroutines)
}
