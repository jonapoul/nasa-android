import nasa.gradle.commonMainDependencies
import nasa.gradle.commonTestDependencies

plugins {
  alias(libs.plugins.module.multiplatform)
  alias(libs.plugins.kotlin.serialization)
}

commonMainDependencies {
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.serialization.core)
  api(libs.kotlinx.serialization.json)
  api(libs.retrofit.core)
  api(projects.apod.model)
  api(projects.core.model)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}

commonTestDependencies {
  implementation(projects.test.http)
  implementation(projects.test.resources)
}
