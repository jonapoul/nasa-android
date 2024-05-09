import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("module-kotlin")
  alias(libs.plugins.kotlin.serialization)
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
  }
}

dependencies {
  api(projects.modules.gallery.model)
  api(projects.modules.core.model)
  api(libs.kotlinx.datetime)
  api(libs.kotlinx.serialization.core)
  api(libs.kotlinx.serialization.json)
  api(libs.retrofit.core)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
  testImplementation(projects.modules.core.http)
  testImplementation(projects.modules.test.http)
  testImplementation(libs.okhttp.core)
  testImplementation(libs.test.okhttp)
}
