plugins {
  id("module-android")
  alias(libs.plugins.ksp)
}

android {
  namespace = "nasa.db"

  ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
  }
}

dependencies {
  api(projects.modules.apod.data.db)
  api(projects.modules.gallery.data.db)
  api(libs.androidx.room.runtime)
  api(libs.kotlinx.coroutines)
  implementation(libs.androidx.room.common)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.sqlite)
  implementation(libs.kotlinx.datetime)
  ksp(libs.androidx.room.compiler)
}
