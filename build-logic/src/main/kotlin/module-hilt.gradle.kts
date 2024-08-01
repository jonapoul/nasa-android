plugins {
  id("module-android")
  id("convention-hilt")
}

val api by configurations

dependencies {
  api(libs.dagger.core)
}
