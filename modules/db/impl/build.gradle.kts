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
  ksp(libs.androidx.room.compiler)

  api(libs.androidx.room.runtime)
  api(libs.kotlinx.coroutines)
  api(projects.modules.db.api)
  api(projects.modules.db.apod)
  api(projects.modules.db.gallery)

  implementation(libs.androidx.room.common)
  implementation(libs.androidx.sqlite)
  implementation(libs.kotlinx.datetime)

  testImplementation(libs.test.alakazam.db)
}
