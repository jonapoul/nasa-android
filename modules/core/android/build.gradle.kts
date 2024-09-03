plugins {
  id("nasa.module.android")
}

android {
  namespace = "nasa.core.android"
}

dependencies {
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  api(projects.core.model)

  implementation(libs.preferences.core)

  testImplementation(projects.test.prefs)
}
