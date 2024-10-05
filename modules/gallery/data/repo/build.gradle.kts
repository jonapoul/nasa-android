plugins {
  id("nasa.module.android")
}

android {
  namespace = "nasa.gallery.data.repo"
}

dependencies {
  api(projects.db)
  api(projects.gallery.data.api)

  implementation(libs.alakazam.kotlin.core)
  implementation(libs.javaxInject)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.preferences.core)
  implementation(libs.retrofit.core)

  testImplementation(libs.androidx.room.runtime)
  testImplementation(libs.test.alakazam.db)
  testImplementation(libs.test.okhttp)
  testImplementation(projects.test.http)
  testImplementation(projects.test.prefs)
}
