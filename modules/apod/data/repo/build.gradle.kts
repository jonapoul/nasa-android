plugins {
  id("module-android")
}

android {
  namespace = "apod.data.repo"
}

dependencies {
  api(projects.modules.apod.data.model)
  api(libs.kotlinx.coroutines)
  implementation(projects.modules.apod.data.api)
  implementation(projects.modules.apod.data.db)
  implementation(projects.modules.core.model)
  implementation(libs.alakazam.kotlin.core)
  implementation(libs.javaxInject)
  implementation(libs.kotlinx.datetime)
}
