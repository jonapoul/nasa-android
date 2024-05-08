plugins {
  id("module-android")
}

android {
  namespace = "nasa.gallery.data.repo"
}

dependencies {
  api(projects.modules.gallery.data.api)
  api(projects.modules.gallery.data.db)
  api(projects.modules.gallery.model)
  api(libs.alakazam.kotlin.core)
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  implementation(libs.androidx.sqlite)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
  testImplementation(projects.modules.test.http)
  testImplementation(libs.test.alakazam.db)
  testImplementation(libs.androidx.room.runtime)
  testImplementation(libs.test.okhttp)
}
