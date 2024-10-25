import nasa.gradle.commonMainDependencies
import nasa.gradle.commonTestDependencies
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.module.multiplatform)
  alias(libs.plugins.kotlin.serialization)
}

tasks.withType<KotlinCompile> {
  compilerOptions {
    freeCompilerArgs.add("-opt-in=kotlinx.serialization.ExperimentalSerializationApi")
  }
}

commonMainDependencies {
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.serialization.core)
  api(libs.kotlinx.serialization.json)
  api(libs.retrofit.core)
  api(projects.gallery.model)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}

commonTestDependencies {
  implementation(libs.kotlinx.immutable)
  implementation(libs.okhttp.core)
  implementation(projects.test.http)
  implementation(projects.test.resources)
}
