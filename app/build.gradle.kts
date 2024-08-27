import blueprint.core.gitVersionCode
import blueprint.core.intProperty
import blueprint.core.javaVersionString
import blueprint.core.rootLocalPropertiesOrNull
import blueprint.core.runGitCommandOrNull
import blueprint.core.stringProperty
import blueprint.core.stringPropertyOrNull
import blueprint.diagrams.ModuleType
import guru.nidi.graphviz.attribute.Rank
import org.gradle.internal.extensions.stdlib.capitalized

plugins {
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.agp.app)
  alias(libs.plugins.hilt)
  alias(libs.plugins.licenses)
  id("nasa.convention.kotlin")
  id("nasa.convention.android.base")
  id("nasa.convention.android.devices")
  id("nasa.convention.compose")
  id("nasa.convention.hilt")
  id("nasa.convention.style")
  id("nasa.convention.test")
  alias(libs.plugins.blueprint.diagrams)
  alias(libs.plugins.dependencySort)
}

enum class NasaModuleType(override val string: String, override val color: String) : ModuleType {
  AndroidApp(string = "Android App", color = "#FF5555"), // red
  AndroidHilt(string = "Android Hilt", color = "#FCB103"), // orange
  AndroidCompose(string = "Android Compose", color = "#FFFF55"), // yellow
  AndroidLibrary(string = "Android Library", color = "#55FF55"), // green
  AndroidResources(string = "Android Resources", color = "#00FFFF"), // cyan
  Kotlin(string = "Kotlin", color = "#A17EFF"), // purple
}

diagramsBlueprint {
  rankDir = Rank.RankDir.TOP_TO_BOTTOM
  rankSeparation = 2.5
  moduleTypes = NasaModuleType.values().toSet()
  moduleTypeFinder = ModuleType.Finder { project ->
    when {
      project.plugins.hasPlugin("com.android.application") -> NasaModuleType.AndroidApp
      project.plugins.hasPlugin("nasa.module.hilt") -> NasaModuleType.AndroidHilt
      project.plugins.hasPlugin("nasa.module.compose") -> NasaModuleType.AndroidCompose
      project.plugins.hasPlugin("nasa.module.android") -> NasaModuleType.AndroidLibrary
      project.plugins.hasPlugin("nasa.module.resources") -> NasaModuleType.AndroidResources
      project.plugins.hasPlugin("nasa.module.kotlin") -> NasaModuleType.Kotlin
      else -> error("Unknown module type for ${project.path}")
    }
  }
}

val gitCommitHash = runGitCommandOrNull(args = listOf("rev-parse", "--short=8", "HEAD"))
  ?: error("Failed getting git version from ${project.path}")

val gitTagOrCommit = runGitCommandOrNull(args = listOf("describe", "--tags", "--abbrev=0")) ?: gitCommitHash

android {
  namespace = "nasa.android"
  compileSdk = intProperty(key = "blueprint.android.compileSdk")

  defaultConfig {
    applicationId = "nasa.android"
    minSdk = intProperty(key = "blueprint.android.minSdk")
    targetSdk = intProperty(key = "blueprint.android.targetSdk")
    versionCode = gitVersionCode()
    versionName = gitTagOrCommit
    setProperty("archivesBaseName", "$applicationId-$versionName")

    val kotlinTime = "kotlinx.datetime.Instant.Companion.fromEpochMilliseconds(${System.currentTimeMillis()}L)"
    buildConfigField("kotlinx.datetime.Instant", "BUILD_TIME", kotlinTime)
    buildConfigField("String", "GIT_HASH", "\"${gitCommitHash}\"")
    val apiKey = stringPropertyOrNull(key = "nasa.apiKey")
    buildConfigField("String", "API_KEY", if (apiKey == null) "null" else "\"${apiKey}\"")

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    multiDexEnabled = true
  }

  kotlinOptions {
    jvmTarget = javaVersionString()
  }

  packaging {
    resources.excludes.addAll(
      listOf(
        "META-INF/DEPENDENCIES",
        "META-INF/INDEX.LIST",
      ),
    )

    jniLibs {
      useLegacyPackaging = true
    }
  }

  buildFeatures {
    buildConfig = true
  }

  signingConfigs {
    val localProps = rootLocalPropertiesOrNull()
    if (localProps != null) {
      create("release") {
        storeFile = rootProject.file(stringProperty(key = "nasa.keyFile"))
        storePassword = stringProperty(key = "nasa.keyFilePassword")
        keyAlias = stringProperty(key = "nasa.keyAlias")
        keyPassword = stringProperty(key = "nasa.keyPassword")
      }
    } else {
      logger.error("No local.properties found - skipping signing configs")
    }
  }

  buildTypes {
    getByName("debug") {
      isMinifyEnabled = false
      isShrinkResources = false
    }

    getByName("release") {
      isMinifyEnabled = true
      isShrinkResources = true
      signingConfig = signingConfigs.findByName("release")
      proguardFiles += listOf(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        file("proguard-rules.pro"),
      )
    }
  }
}

hilt {
  enableAggregatingTask = true
  enableExperimentalClasspathAggregation = true
}

// Used to populate the licenses screen in-app
licenseReport {
  // Generate JSON and place in app assets
  generateJsonReport = true
  copyJsonReportToAssets = true

  // Disable other report outputs
  generateHtmlReport = false
  generateCsvReport = false
  generateTextReport = false
}

fun registerLicenseTask(suffix: String) {
  val capitalized = suffix.capitalized()
  val assemble = tasks.getByName("assemble$capitalized")
  val license = tasks.getByName("license${capitalized}Report")
  assemble.dependsOn(license)
  license.doFirst {
    val file = project.file("src/main/assets/open_source_licenses.json")
    file.delete()
  }
}

afterEvaluate {
  registerLicenseTask(suffix = "debug")
  registerLicenseTask(suffix = "release")
}

dependencies {
  implementation(libs.alakazam.android.core)
  implementation(libs.alakazam.kotlin.core)
  implementation(libs.alakazam.kotlin.time)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.activity.core)
  implementation(libs.androidx.activity.ktx)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.savedstate)
  implementation(libs.androidx.room.runtime)
  implementation(libs.androidx.splash)
  implementation(libs.coil.base)
  implementation(libs.coil.core)
  implementation(libs.dagger.core)
  implementation(libs.hilt.android)
  implementation(libs.hilt.core)
  implementation(libs.javaxInject)
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.datetime)
  implementation(libs.material)
  implementation(libs.okhttp.core)
  implementation(libs.preferences.core)
  implementation(libs.voyager.core)
  implementation(libs.voyager.navigator)
  implementation(projects.about.di)
  implementation(projects.about.nav)
  implementation(projects.about.ui)
  implementation(projects.apod.di)
  implementation(projects.apod.nav)
  implementation(projects.apod.ui.grid)
  implementation(projects.apod.ui.single)
  implementation(projects.core.di)
  implementation(projects.db.di)
  implementation(projects.gallery.di)
  implementation(projects.gallery.nav)
  implementation(projects.gallery.ui.image)
  implementation(projects.gallery.ui.search)
  implementation(projects.home.nav)
  implementation(projects.home.ui)
  implementation(projects.licenses.di)
  implementation(projects.licenses.nav)
  implementation(projects.licenses.ui)
  implementation(projects.settings.nav)
  implementation(projects.settings.ui)

  testImplementation(projects.test.prefs)
}
