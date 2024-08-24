plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.settings.vm"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.alakazam.kotlin.core)
  api(libs.kotlinx.coroutines)
  api(projects.apod.data.repo)
  api(projects.core.url)
  api(projects.db.api)

  compileOnly(libs.alakazam.kotlin.compose.annotations)

  testImplementation(libs.androidx.room.runtime)
  testImplementation(libs.test.alakazam.db)
  testImplementation(projects.db.impl)
}
