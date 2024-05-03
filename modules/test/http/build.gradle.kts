plugins {
  id("module-kotlin")
}

dependencies {
  api(libs.test.okhttp)
  implementation(projects.modules.core.http)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
  implementation(libs.test.kotlin.junit)
}
