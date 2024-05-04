plugins {
  id("module-hilt")
}

android {
  namespace = "apod.about.di"
}

dependencies {
  api(projects.modules.about.data)
  api(projects.modules.core.http)
  api(libs.alakazam.android.core)
  api(libs.javaxInject)
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
}
