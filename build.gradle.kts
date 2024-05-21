import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import javax.xml.parsers.DocumentBuilderFactory

plugins {
  alias(libs.plugins.agp.app) apply false
  alias(libs.plugins.agp.lib) apply false
  alias(libs.plugins.detekt) apply false
  alias(libs.plugins.hilt) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.kover) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.ktlint) apply false
  alias(libs.plugins.licensee) apply false
  alias(libs.plugins.licenses) apply false
  alias(libs.plugins.spotless) apply false

  alias(libs.plugins.dependencyAnalysis)
  alias(libs.plugins.dependencyGuard)
  alias(libs.plugins.dependencyVersions)
  alias(libs.plugins.doctor)

  id("convention-test")
}

dependencyAnalysis {
  structure {
    ignoreKtx(ignore = true)
    bundle(name = "kotlin-stdlib") { includeGroup(group = "org.jetbrains.kotlin") }
    bundle(name = "modules") { include("^:.*\$".toRegex()) }
    bundle(name = "okhttp") { includeGroup(group = "com.squareup.okhttp3") }
    bundle(name = "viewModel") { include(regex = "androidx.lifecycle:lifecycle-viewmodel.*".toRegex()) }
  }

  issues {
    all {
      onAny { severity(value = "fail") }

      onRuntimeOnly { severity(value = "ignore") }

      onIncorrectConfiguration {
        exclude(
          ":modules:core:http",
          ":modules:core:model",
        )
      }

      onUnusedDependencies {
        exclude("com.squareup.okhttp3:okhttp")
        exclude(
          libs.test.alakazam.core,
          libs.test.androidx.arch,
          libs.test.androidx.junit,
          libs.test.androidx.rules,
          libs.test.androidx.runner,
          libs.test.hilt,
          libs.test.junit,
          libs.test.mockk.android,
          libs.test.mockk.dsl,
          libs.test.robolectric,
          libs.test.timber,
          libs.test.turbine,
        )
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

dependencyGuard {
  configuration("classpath")
}

tasks.withType<DependencyUpdatesTask> {
  rejectVersionIf { !candidate.version.isStable() && currentVersion.isStable() }
}

private fun String.isStable(): Boolean = listOf("alpha", "beta", "rc").none { lowercase().contains(it) }
