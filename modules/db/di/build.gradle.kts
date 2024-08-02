plugins {
  id("module-hilt")
}

android {
  namespace = "nasa.db.di"
}

dependencies {
  api(projects.modules.db.impl)
  api(libs.alakazam.android.core)
  api(libs.javaxInject)
  implementation(libs.androidx.room.runtime)
  implementation(libs.kotlin.stdlib)
  implementation(libs.timber)
}
