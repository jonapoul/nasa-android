plugins {
  id("nasa.module.hilt")
}

android {
  namespace = "nasa.apod.di"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(projects.apod.data.api)
  api(projects.core.http.usage)

  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
  implementation(projects.core.http.factories)
}
