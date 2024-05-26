plugins {
  id("module-viewmodel")
}

android {
  namespace = "nasa.settings.vm"
}

dependencies {
  api(projects.modules.core.db)
  api(projects.modules.core.url)
  api(projects.modules.apod.data.repo)
  api(libs.alakazam.android.core)
  api(libs.alakazam.kotlin.core)
  api(libs.kotlinx.coroutines)
  implementation(libs.androidx.sqlite)
  implementation(libs.timber)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
  testImplementation(libs.androidx.room.runtime)
  testImplementation(libs.test.alakazam.db)
}
