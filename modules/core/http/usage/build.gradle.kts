import nasa.gradle.commonMainDependencies
import nasa.gradle.commonTestDependencies

plugins {
  alias(libs.plugins.module.multiplatform)
  alias(libs.plugins.kotlin.serialization)
}

commonMainDependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.javaxInject)
  api(libs.okhttp.core)

  implementation(libs.kotlinx.coroutines)
}

commonTestDependencies {
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.retrofit.core)
  implementation(projects.core.http.factories)
  implementation(projects.test.flow)
  implementation(projects.test.http)
}
