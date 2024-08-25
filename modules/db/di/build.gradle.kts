plugins {
  id("nasa.module.hilt")
}

android {
  namespace = "nasa.db.di"
}

dependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(projects.db.api)
  api(projects.db.impl)
}
