plugins {
  id("nasa.module.hilt")
}

android {
  namespace = "nasa.about.di"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(projects.about.data)

  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
  implementation(projects.core.http.factories)
}
