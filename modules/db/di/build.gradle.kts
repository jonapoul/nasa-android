plugins {
  id("module-hilt")
}

android {
  namespace = "nasa.db.di"
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.javaxInject)
  api(projects.modules.db.impl)

  implementation(libs.androidx.room.runtime)
  implementation(libs.kotlin.stdlib)
}
