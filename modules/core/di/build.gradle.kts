plugins {
  id("nasa.module.hilt")
}

android {
  namespace = "nasa.core.di"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.alakazam.kotlin.core)
  api(libs.alakazam.kotlin.time)
  api(libs.androidx.preference.ktx)
  api(libs.dagger.core)
  api(libs.hilt.android)
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  api(libs.preferences.core)
  api(projects.core.android)

  implementation(libs.preferences.android)
  implementation(projects.core.http)
}
