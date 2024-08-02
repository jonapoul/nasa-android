plugins {
  id("module-android")
  alias(libs.plugins.ksp)
}

android {
  namespace = "nasa.db.impl"

  ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
  }
}

dependencies {
  api(projects.modules.db.apod)
  api(projects.modules.db.api)
  api(projects.modules.db.gallery)
  api(libs.androidx.room.runtime)
  api(libs.kotlinx.coroutines)
  implementation(libs.androidx.room.common)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.sqlite)
  implementation(libs.kotlinx.datetime)
  ksp(libs.androidx.room.compiler)
  testImplementation(libs.test.alakazam.db)
}
