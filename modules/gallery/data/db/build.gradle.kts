plugins {
  id("module-android")
}

android {
  namespace = "nasa.gallery.data.db"
}

dependencies {
  api(projects.modules.gallery.model)
  api(libs.androidx.room.runtime)
  api(libs.kotlinx.coroutines)
  implementation(libs.androidx.room.common)
  implementation(libs.kotlinx.datetime)
}
