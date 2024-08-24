plugins {
  id("nasa.module.android")
}

android {
  namespace = "nasa.apod.data.repo"
}

dependencies {
  api(libs.kotlinx.coroutines)
  api(projects.apod.data.api)

  implementation(libs.alakazam.kotlin.core)
  implementation(libs.javaxInject)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
  implementation(libs.timber)
  implementation(projects.db.apod)

  testImplementation(libs.androidx.room.runtime)
  testImplementation(libs.test.alakazam.db)
  testImplementation(libs.test.okhttp)
  testImplementation(projects.db.impl)
  testImplementation(projects.test.http)
}
