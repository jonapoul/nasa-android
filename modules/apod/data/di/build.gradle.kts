plugins {
  id("module-hilt")
}

android {
  namespace = "apod.data.di"
}

dependencies {
  api(projects.modules.apod.data.api)
  api(projects.modules.apod.data.db)
  api(libs.javaxInject)
  implementation(projects.modules.core.http)
  implementation(libs.androidx.room.runtime)
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
}
