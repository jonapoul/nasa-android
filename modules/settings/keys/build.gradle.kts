plugins {
  id("module-android")
}

android {
  namespace = "apod.settings.keys"
}

dependencies {
  api(projects.modules.core.model)
  api(libs.alakazam.android.prefs)
}
