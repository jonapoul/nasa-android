import blueprint.core.getStringOrThrow
import blueprint.core.gitVersionCode
import blueprint.core.gitVersionName
import blueprint.core.intProperty
import blueprint.core.rootLocalProperties
import blueprint.core.rootLocalPropertiesOrNull

plugins {
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.agp.app)
  alias(libs.plugins.hilt)
  alias(libs.plugins.licenses)
  id("convention-kotlin")
  id("convention-android-base")
  id("convention-compose")
  id("convention-diagrams")
  id("convention-hilt")
  id("convention-style")
  id("convention-test")
  alias(libs.plugins.dependencyGuard)
}

dependencyGuard {
  configuration("releaseRuntimeClasspath")
}

android {
  namespace = "apod.android"
  compileSdk = intProperty(key = "apod.compileSdk")

  defaultConfig {
    applicationId = "apod.android"
    minSdk = intProperty(key = "apod.minSdk")
    targetSdk = intProperty(key = "apod.targetSdk")
    versionCode = gitVersionCode()
    versionName = gitVersionName()

    val kotlinTime = "kotlinx.datetime.Instant.Companion.fromEpochMilliseconds(${System.currentTimeMillis()}L)"
    buildConfigField("kotlinx.datetime.Instant", "BUILD_TIME", kotlinTime)
    buildConfigField("String", "GIT_HASH", "\"${versionName}\"")
    val apiKey = rootLocalProperties(filename = "local-api.properties").getStringOrThrow(key = "nasaApiKey")
    if (apiKey.isBlank()) error("Empty nasaApiKey property")
    buildConfigField("String", "API_KEY", "\"${apiKey}\"")

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    multiDexEnabled = true
  }

  kotlinOptions {
    jvmTarget = "19"
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
    resValues = true
  }

  signingConfigs {
    val localProps = rootLocalPropertiesOrNull(filename = "local-keystore.properties")
    if (localProps != null) {
      create("release") {
        storeFile = rootProject.file(localProps.getStringOrThrow(key = "keyFile"))
        storePassword = localProps.getStringOrThrow(key = "keyFilePassword")
        keyAlias = localProps.getStringOrThrow(key = "keyAlias")
        keyPassword = localProps.getStringOrThrow(key = "keyPassword")
      }
    } else {
      logger.error("No local-keystore.properties found - skipping signing configs")
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

afterEvaluate {
  // Make sure the licenses JSON is generated whenever we rebuild the app
  val licenseReleaseReport by tasks
  val preBuild by tasks
  val assembleDebug by tasks
  val assembleRelease by tasks
  preBuild.dependsOn(licenseReleaseReport)
  assembleDebug.dependsOn(licenseReleaseReport)
  assembleRelease.dependsOn(licenseReleaseReport)

  // Delete the existing JSON file before we regenerate it. If we don't do this, the plugin
  // doesn't overwrite the existing one, so any dependency changes won't be reflected
  licenseReleaseReport.doFirst {
    val file = project.file("src/main/assets/open_source_licenses.json")
    file.delete()
  }
}

dependencies {
  implementation(projects.modules.about.di)
  implementation(projects.modules.about.ui)
  implementation(projects.modules.apod.data.di)
  implementation(projects.modules.apod.screenSingle.ui)
  implementation(projects.modules.core.http)
  implementation(projects.modules.core.model)
  implementation(projects.modules.core.res)
  implementation(projects.modules.core.theme)
  implementation(projects.modules.core.ui)
  implementation(projects.modules.licenses.di)
  implementation(projects.modules.licenses.ui)
  implementation(projects.modules.navigation)
  implementation(projects.modules.settings.ui)

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
  implementation(libs.androidx.preference.ktx)
  implementation(libs.androidx.splash)
  implementation(libs.coil.base)
  implementation(libs.coil.core)
  implementation(libs.dagger.core)
  implementation(libs.flowpreferences)
  implementation(libs.hilt.android)
  implementation(libs.javaxInject)
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.datetime)
  implementation(libs.logback)
  implementation(libs.material)
  implementation(libs.okhttp.core)
  implementation(libs.timber)
  implementation(libs.voyager.core)
  implementation(libs.voyager.navigator)
}
