plugins {
  id("module-hilt")
}

android {
  namespace = "nasa.apod.di"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.javaxInject)
  api(projects.modules.apod.data.api)

  implementation(libs.androidx.room.runtime)
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
  implementation(libs.timber)
  implementation(projects.modules.core.http)
}
