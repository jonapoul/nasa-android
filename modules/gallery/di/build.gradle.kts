plugins {
  id("module-hilt")
}

android {
  namespace = "nasa.gallery.data.di"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.javaxInject)
  api(projects.modules.gallery.data.api)

  implementation(libs.androidx.room.runtime)
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
  implementation(libs.timber)
  implementation(projects.modules.core.http)
  implementation(projects.modules.core.model)
}
