plugins {
  id("nasa.module.android")
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
  api(projects.db.api)
  api(projects.db.apod)
  api(projects.db.gallery)

  implementation(libs.androidx.room.common)
  implementation(libs.androidx.sqlite.core)
  implementation(libs.javaxInject)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)

  testImplementation(libs.androidx.sqlite.framework)
  testImplementation(libs.test.alakazam.db)
  testImplementation(libs.test.androidx.monitor)
  testImplementation(libs.test.androidx.room)
}
