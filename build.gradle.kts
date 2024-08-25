import blueprint.core.rootLocalProperties
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
  alias(libs.plugins.agp.app) apply false
  alias(libs.plugins.agp.lib) apply false
  alias(libs.plugins.androidx.room) apply false
  alias(libs.plugins.androidCacheFix) apply false
  alias(libs.plugins.blueprint.diagrams) apply false
  alias(libs.plugins.detekt) apply false
  alias(libs.plugins.hilt) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.powerAssert) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.ktlint) apply false
  alias(libs.plugins.licensee) apply false
  alias(libs.plugins.licenses) apply false
  alias(libs.plugins.spotless) apply false

  alias(libs.plugins.dependencyAnalysis)
  alias(libs.plugins.dependencySort)
  alias(libs.plugins.dependencyVersions)
  alias(libs.plugins.doctor)
  alias(libs.plugins.kover)

  id("nasa.convention.test")
}

// Place all local properties in the project-level gradle properties map
rootLocalProperties().forEach { (key, value) ->
  ext[key.toString()] = value.toString()
}

dependencyAnalysis {
  structure {
    ignoreKtx(ignore = true)
    bundle(name = "kotlin-stdlib") { includeGroup(group = "org.jetbrains.kotlin") }
    bundle(name = "modules") { include("^:.*\$".toRegex()) }
    bundle(name = "okhttp") { includeGroup(group = "com.squareup.okhttp3") }
    bundle(name = "viewModel") { include(regex = "androidx.lifecycle:lifecycle-viewmodel.*".toRegex()) }
    bundle(name = "voyagerCore") { include(regex = "cafe.adriel.voyager:voyager-core.*".toRegex()) }
  }

  issues {
    all {
      onAny { severity(value = "fail") }

      onRuntimeOnly { severity(value = "ignore") }

      onIncorrectConfiguration {
        exclude(
          ":core:model",
          ":core:res",
          "javax.inject:javax.inject",
        )
      }

      onUnusedDependencies {
        exclude(
          libs.test.alakazam.core,
          libs.test.androidx.arch,
          libs.test.androidx.junit,
          libs.test.androidx.rules,
          libs.test.androidx.runner,
          libs.test.hilt,
          libs.test.junit,
          libs.test.mockk.android,
          libs.test.mockk.core,
          libs.test.mockk.dsl,
          libs.test.robolectric,
          libs.test.timber,
          libs.test.turbine,
          libs.androidx.compose.ui.toolingPreview,
          libs.timber,
        )
        exclude(":test:resources")
      }
    }
  }
}

doctor {
  javaHome {
    ensureJavaHomeMatches = false
    ensureJavaHomeIsSet = true
    failOnError = true
  }
}

tasks.withType<DependencyUpdatesTask> {
  rejectVersionIf { !candidate.version.isStable() && currentVersion.isStable() }
}

private fun String.isStable(): Boolean = listOf("alpha", "beta", "rc").none { lowercase().contains(it) }
