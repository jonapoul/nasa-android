plugins {
  alias(libs.plugins.module.hilt)
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(projects.about.data)
  api(projects.core.http.factories)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
}
