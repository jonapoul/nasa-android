plugins {
  id("module-android")
}

android {
  namespace = "nasa.gallery.data.repo"
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  api(projects.modules.db.gallery)
  api(projects.modules.gallery.data.api)

  implementation(libs.androidx.sqlite)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)

  testImplementation(libs.androidx.room.runtime)
  testImplementation(libs.test.alakazam.db)
  testImplementation(libs.test.okhttp)
  testImplementation(projects.modules.test.http)
}
