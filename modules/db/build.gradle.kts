import nasa.gradle.commonMainDependencies
import nasa.gradle.commonTestDependencies
import nasa.gradle.jvmMainDependencies

plugins {
  alias(libs.plugins.module.multiplatform)
  alias(libs.plugins.ksp)
  alias(libs.plugins.androidx.room)
  alias(libs.plugins.buildconfig)
}

val schemaDir = projectDir.resolve("schemas")

room {
  generateKotlin = true
  schemaDirectory(schemaDir.absolutePath)
}

buildConfig {
  packageName("nasa.db")
  sourceSets.getByName("test") {
    buildConfigField(name = "SCHEMAS_PATH", schemaDir)
  }
}

commonMainDependencies {
  api(libs.androidx.room.runtime)
  api(libs.kotlinx.coroutines)
  api(projects.gallery.model)
  implementation(libs.androidx.room.common)
  implementation(libs.androidx.sqlite.core)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
}

commonTestDependencies {
  implementation(libs.test.alakazam.core)
  implementation(libs.test.androidx.room)
  implementation(projects.test.flow)
}

jvmMainDependencies {
  implementation(libs.androidx.sqlite.bundled)
}

dependencies {
  listOf(
    "kspAndroid",
    "kspAndroidTest",
    "kspCommonMainMetadata",
    "kspJvm",
    "kspJvmTest",
  ).forEach { config -> add(config, libs.androidx.room.compiler) }
}
