plugins {
  id("module-android")
}

android {
  namespace = "apod.data.repo"
}

dependencies {
  api(projects.modules.core.model)
  api(libs.kotlinx.coroutines)
  implementation(projects.modules.apod.data.api)
  implementation(projects.modules.apod.data.db)
  implementation(libs.alakazam.kotlin.core)
  implementation(libs.javaxInject)
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
