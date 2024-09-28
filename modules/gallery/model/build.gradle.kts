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
  api(libs.kotlinx.immutable)
  api(libs.kotlinx.serialization.core)
  api(libs.kotlinx.serialization.json)
}
