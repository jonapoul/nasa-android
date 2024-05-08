plugins {
  id("module-hilt")
}

android {
  namespace = "nasa.apod.di"
}

dependencies {
  api(projects.modules.apod.data.api)
  api(projects.modules.apod.data.db)
  api(projects.modules.core.http)
  api(libs.alakazam.android.core)
  api(libs.javaxInject)
  implementation(libs.androidx.room.runtime)
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
}
