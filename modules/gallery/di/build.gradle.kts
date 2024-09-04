plugins {
  id("nasa.module.hilt")
}

android {
  namespace = "nasa.gallery.di"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(projects.core.http.factories)
  api(projects.gallery.data.api)

  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
}
