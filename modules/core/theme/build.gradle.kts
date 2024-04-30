plugins {
  id("module-android")
}

android {
  namespace = "apod.core.theme"
}

dependencies {
  api(projects.modules.core.model)
  api(libs.flowpreferences)
  api(libs.javaxInject)
  implementation(projects.modules.settings.keys)
  implementation(libs.alakazam.android.prefs)
}
