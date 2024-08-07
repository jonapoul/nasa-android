plugins {
  id("nasa.module.android")
}

android {
  namespace = "nasa.gallery.data.repo"
}

dependencies {
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  api(projects.modules.db.gallery)
  api(projects.modules.gallery.data.api)

  implementation(libs.alakazam.kotlin.core)
  implementation(libs.androidx.sqlite.core)
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
  testImplementation(projects.modules.db.impl)
  testImplementation(projects.modules.test.http)
  testImplementation(projects.modules.test.prefs)
}
