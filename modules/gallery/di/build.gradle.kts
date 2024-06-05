plugins {
  id("module-hilt")
}

android {
  namespace = "nasa.gallery.data.di"
}

dependencies {
  api(projects.modules.gallery.data.api)
  api(libs.alakazam.android.core)
  api(libs.javaxInject)
  implementation(projects.modules.core.http)
  implementation(projects.modules.core.model)
  implementation(libs.androidx.room.runtime)
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
  implementation(libs.timber)
}
