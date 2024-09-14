plugins {
  id("nasa.module.viewmodel")
}

android {
  namespace = "nasa.settings.vm"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.alakazam.kotlin.core)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(projects.apod.data.repo)
  api(projects.db.api)

  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.hilt.core)
  implementation(libs.kotlinx.coroutines)

  testImplementation(libs.androidx.room.runtime)
  testImplementation(libs.test.alakazam.db)
  testImplementation(projects.db.impl)
}
