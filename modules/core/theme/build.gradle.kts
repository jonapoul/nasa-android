plugins {
  id("module-android")
}

android {
  namespace = "apod.core.theme"
}

dependencies {
  api(projects.modules.core.model)
  api(libs.flowpreferences)
  implementation(libs.alakazam.android.prefs)
  implementation(libs.alakazam.kotlin.core)
  implementation(libs.javaxInject)
}
