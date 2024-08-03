plugins {
  id("module-viewmodel")
}

android {
  namespace = "nasa.settings.vm"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.alakazam.kotlin.core)
  api(libs.kotlinx.coroutines)
  api(projects.modules.apod.data.repo)
  api(projects.modules.core.url)
  api(projects.modules.db.api)

  implementation(libs.timber)

  compileOnly(libs.alakazam.kotlin.compose.annotations)

  testImplementation(libs.androidx.room.runtime)
  testImplementation(libs.test.alakazam.db)
  testImplementation(projects.modules.db.impl)
}
