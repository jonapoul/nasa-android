import nasa.gradle.commonMainDependencies
import nasa.gradle.commonTestDependencies

plugins {
  alias(libs.plugins.module.multiplatform)
}

commonMainDependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.javaxInject)
  api(libs.okhttp.core)
  api(projects.core.model)

  implementation(libs.kotlinx.coroutines)
  implementation(libs.okio)
}

commonTestDependencies {
  implementation(libs.retrofit.core)
  implementation(libs.retrofit.scalars)
  implementation(projects.core.http.factories)
  implementation(projects.test.flow)
  implementation(projects.test.http)
}
