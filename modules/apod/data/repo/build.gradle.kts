import nasa.gradle.androidUnitTestDependencies
import nasa.gradle.commonMainDependencies

plugins {
  alias(libs.plugins.module.multiplatform)
}

commonMainDependencies {
  api(projects.apod.data.api)
  api(projects.db)

  implementation(libs.alakazam.kotlin.core)
  implementation(libs.javaxInject)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.retrofit.core)
}

androidUnitTestDependencies {
  implementation(libs.androidx.room.runtime)
  implementation(libs.test.alakazam.db)
  implementation(projects.test.flow)
  implementation(projects.test.http)
  implementation(projects.test.resources)
}
