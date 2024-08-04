plugins {
  id("module-android")
  alias(libs.plugins.ksp)
  alias(libs.plugins.androidx.room)
}

val schemaDir = "$projectDir/schemas"

android {
  namespace = "nasa.db.impl"

  sourceSets {
    getByName("test").assets.srcDirs(schemaDir)
  }
}

room {
  generateKotlin = true
  schemaDirectory(schemaDir)
}

dependencies {
  ksp(libs.androidx.room.compiler)

  api(libs.alakazam.kotlin.core)
  api(libs.androidx.room.runtime)
  api(libs.kotlinx.coroutines)
  api(projects.modules.db.api)
  api(projects.modules.db.apod)
  api(projects.modules.db.gallery)

  implementation(libs.androidx.room.common)
  implementation(libs.androidx.sqlite)
  implementation(libs.kotlinx.datetime)

  testImplementation(libs.test.alakazam.db)
  testImplementation(libs.test.androidx.room)
}
