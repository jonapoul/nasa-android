plugins {
  id("module-android")
}

android {
  namespace = "nasa.apod.data.repo"
}

dependencies {
  api(projects.modules.apod.data.api)
  api(projects.modules.apod.data.db)
  api(libs.alakazam.kotlin.core)
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  implementation(libs.androidx.sqlite)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
  implementation(libs.timber)
  testImplementation(projects.modules.test.http)
  testImplementation(libs.test.alakazam.db)
  testImplementation(libs.androidx.room.runtime)
  testImplementation(libs.test.okhttp)
}
