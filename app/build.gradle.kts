import blueprint.core.gitVersionCode
import blueprint.core.intProperty
import blueprint.core.javaVersionString
import blueprint.core.rootLocalPropertiesOrNull
import blueprint.core.runGitCommandOrNull
import blueprint.core.stringProperty
import blueprint.core.stringPropertyOrNull
import org.gradle.internal.extensions.stdlib.capitalized
import java.time.LocalDate

plugins {
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.agp.app)
  alias(libs.plugins.hilt)
  alias(libs.plugins.licenses)
  id("nasa.convention.kotlin")
  id("nasa.convention.android.base")
  id("nasa.convention.android.devices")
  id("nasa.convention.compose")
  id("nasa.convention.diagrams")
  id("nasa.convention.hilt")
  id("nasa.convention.style")
  id("nasa.convention.test")
  alias(libs.plugins.dependencySort)
}

val gitCommitHash = runGitCommandOrNull(args = listOf("rev-parse", "--short=8", "HEAD"))
  ?: error("Failed getting git version from ${project.path}")

fun versionCode(): String {
  val date = LocalDate.now()
  return "%04d.%02d.%02d".format(date.year, date.monthValue, date.dayOfMonth)
}

android {
  namespace = "nasa.android"
  compileSdk = intProperty(key = "blueprint.android.compileSdk")

  defaultConfig {
    applicationId = "nasa.android"
    minSdk = intProperty(key = "blueprint.android.minSdk")
    targetSdk = intProperty(key = "blueprint.android.targetSdk")
    versionCode = gitVersionCode()
    versionName = versionCode()
    multiDexEnabled = true
    setProperty("archivesBaseName", "$applicationId-$versionName")

    val kotlinTime = "kotlinx.datetime.Instant.Companion.fromEpochMilliseconds(${System.currentTimeMillis()}L)"
    buildConfigField("kotlinx.datetime.Instant", "BUILD_TIME", kotlinTime)
    buildConfigField("String", "GIT_HASH", "\"${gitCommitHash}\"")
    val apiKey = stringPropertyOrNull(key = "nasa.apiKey")
    buildConfigField("String", "API_KEY", if (apiKey == null) "null" else "\"${apiKey}\"")
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
      enableUnitTestCoverage = true
      enableAndroidTestCoverage = true
    }

    getByName("release") {
      isMinifyEnabled = true
      isShrinkResources = true
      enableUnitTestCoverage = false
      enableAndroidTestCoverage = false
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
  implementation(libs.androidx.navigation.compose)
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
  implementation(libs.molecule)
  implementation(libs.okhttp.core)
  implementation(libs.preferences.core)
  implementation(projects.about.di)
  implementation(projects.about.ui)
  implementation(projects.apod.di)
  implementation(projects.apod.ui.full)
  implementation(projects.apod.ui.grid)
  implementation(projects.apod.ui.single)
  implementation(projects.core.di)
  implementation(projects.db.di)
  implementation(projects.gallery.di)
  implementation(projects.gallery.ui.image)
  implementation(projects.gallery.ui.search)
  implementation(projects.home.nav)
  implementation(projects.home.ui)
  implementation(projects.licenses.di)
  implementation(projects.licenses.ui)
  implementation(projects.settings.ui)

  testImplementation(projects.test.prefs)
}
