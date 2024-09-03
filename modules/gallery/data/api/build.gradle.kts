import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("nasa.module.kotlin")
  alias(libs.plugins.kotlin.serialization)
}

tasks.withType<KotlinCompile> {
  compilerOptions {
    freeCompilerArgs.add("-opt-in=kotlinx.serialization.ExperimentalSerializationApi")
  }
}

dependencies {
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.serialization.core)
  api(libs.kotlinx.serialization.json)
  api(libs.retrofit.core)
  api(projects.gallery.model)

  compileOnly(libs.alakazam.kotlin.compose.annotations)

  testImplementation(libs.kotlinx.immutable)
  testImplementation(libs.okhttp.core)
  testImplementation(libs.test.okhttp)
  testImplementation(projects.test.http)
}
