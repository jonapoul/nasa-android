plugins {
  id("module-android")
}

android {
  namespace = "nasa.apod.data.db"
}

dependencies {
  api(projects.modules.apod.model)
  api(libs.androidx.room.runtime)
  api(libs.kotlinx.coroutines)
  implementation(libs.androidx.room.common)
  implementation(libs.kotlinx.datetime)
  testImplementation(projects.modules.core.db)
  testImplementation(libs.androidx.room.runtime)
  testImplementation(libs.test.alakazam.db)
}
