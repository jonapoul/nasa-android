@file:Suppress("UnstableApiUsage")

import blueprint.core.intProperty
import com.android.build.api.dsl.CommonExtension
import nasa.gradle.javaVersion
import org.gradle.accessors.dm.LibrariesForLibs

extensions.getByType(CommonExtension::class).apply {
  compileSdk = intProperty(key = "nasa.compileSdk")

  defaultConfig {
    minSdk = intProperty(key = "nasa.minSdk")
  }

  val javaVersion = javaVersion()
  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
  }

  buildFeatures {
    // Enabled in modules that need them
    buildConfig = false
    compose = false
    resValues = false

    // Disable useless build steps
    aidl = false
    prefab = false
    renderScript = false
    shaders = false
    viewBinding = false
  }

  lint {
    abortOnError = true
    checkGeneratedSources = false
    checkReleaseBuilds = false
    checkTestSources = true
    explainIssues = true
    htmlReport = true
    xmlReport = true
    lintConfig = rootProject.file("lint.xml")
  }

  testOptions {
    unitTests {
      isIncludeAndroidResources = true
      isReturnDefaultValues = true
    }
  }
}

val libs = the<LibrariesForLibs>()
val coreLibraryDesugaring by configurations

dependencies {
  coreLibraryDesugaring(libs.android.desugaring)
}
