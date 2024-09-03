plugins {
  id("nasa.module.kotlin")
}

dependencies {
  api(libs.test.junit)
  api(libs.test.okhttp)

  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
  implementation(libs.test.kotlin.junit)
  implementation(projects.core.http.factories)
}
