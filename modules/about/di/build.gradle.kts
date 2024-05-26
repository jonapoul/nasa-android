plugins {
  id("module-hilt")
}

android {
  namespace = "nasa.about.di"
}

dependencies {
  api(projects.modules.about.data)
  api(libs.alakazam.android.core)
  api(libs.javaxInject)
  implementation(projects.modules.core.http)
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
  implementation(libs.timber)
}
