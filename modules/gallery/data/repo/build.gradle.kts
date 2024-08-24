plugins {
  id("nasa.module.android")
}

android {
  namespace = "nasa.gallery.data.repo"
}

dependencies {
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  api(projects.db.gallery)
  api(projects.gallery.data.api)

  implementation(libs.alakazam.kotlin.core)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.preferences.core)
  implementation(libs.retrofit.core)
  implementation(libs.timber)

  testImplementation(libs.androidx.room.runtime)
  testImplementation(libs.test.alakazam.db)
  testImplementation(libs.test.okhttp)
  testImplementation(projects.db.impl)
  testImplementation(projects.test.http)
  testImplementation(projects.test.prefs)
}
