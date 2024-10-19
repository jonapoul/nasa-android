plugins {
  id("nasa.module.multiplatform")
  id("nasa.convention.coroutines")
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

android {
  namespace = "nasa.db"
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(libs.androidx.room.runtime)
      api(libs.kotlinx.coroutines)
      api(projects.gallery.model)

      implementation(libs.androidx.room.common)
      implementation(libs.androidx.sqlite.core)
      implementation(libs.kotlinx.datetime)
      implementation(libs.kotlinx.serialization.core)
      implementation(libs.kotlinx.serialization.json)
    }

    commonTest.dependencies {
      implementation(libs.test.alakazam.core)
      implementation(libs.test.androidx.room)
      implementation(projects.test.flow)
    }

    jvmMain.dependencies {
      implementation(libs.androidx.sqlite.bundled)
    }
  }
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
