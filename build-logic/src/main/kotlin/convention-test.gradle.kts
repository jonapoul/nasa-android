@file:Suppress("UnstableApiUsage")

import blueprint.core.intProperty
import com.android.build.gradle.api.AndroidBasePlugin
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.MetricType
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
  id("org.jetbrains.kotlinx.kover")
}

val isRootProject = project == rootProject
val isAndroid = project.plugins.any { it is AndroidBasePlugin }

val androidVariant = when {
  isAndroid -> "debug"
  else -> null // kotlin JVM, no variant to merge with
}

koverReport {
  defaults {
    if (androidVariant != null) {
      mergeWith(androidVariant)
    }

    filters {
      excludes {
        classes(
          "*Activity*",
          "*Application*",
          "*BuildConfig*",
          "*ComposableSingletons*",
          "*_Factory*",
          "*_HiltModules*",
          "*_Impl*",
          "*Module_*",
          "hilt_aggregated_deps*",
        )

        annotatedBy(
          "androidx.compose.runtime.Composable",
          "dagger.Generated",
          "dagger.Module",
          "javax.annotation.processing.Generated",
        )
      }
    }

    html { onCheck = true }
    xml { onCheck = true }

    log {
      onCheck = true
      coverageUnits = MetricType.INSTRUCTION
      aggregationForGroup = AggregationType.COVERED_PERCENTAGE
    }

    verify {
      onCheck = true
      rule {
        isEnabled = isRootProject
        bound {
          minValue = intProperty(key = "blueprint.kover.minCoverage")
          metric = MetricType.INSTRUCTION
          aggregation = AggregationType.COVERED_PERCENTAGE
        }
      }
    }
  }

  if (androidVariant != null) {
    androidReports(androidVariant) {
      // No-op, all same config as default
    }
  }
}

val testImplementation = configurations.findByName("testImplementation")

dependencies {
  testImplementation?.let { testImplementation ->
    testImplementation(libs.test.alakazam.core)
    testImplementation(libs.test.junit)
    testImplementation(libs.test.kotlin.common)
    testImplementation(libs.test.kotlin.junit)
    testImplementation(libs.test.kotlinx.coroutines)
    testImplementation(libs.test.mockk.core)
    testImplementation(libs.test.mockk.dsl)
    testImplementation(libs.test.turbine)

    if (isAndroid) {
      testImplementation(libs.test.androidx.arch)
      testImplementation(libs.test.androidx.coreKtx)
      testImplementation(libs.test.androidx.junit)
      testImplementation(libs.test.androidx.rules)
      testImplementation(libs.test.androidx.runner)
      testImplementation(libs.test.mockk.android)
      testImplementation(libs.test.robolectric)
      testImplementation(libs.test.timber)
    }
  }
}

rootProject.dependencies {
  // Include this module in test coverage
  kover(project)
}

tasks.withType<Test> {
  testLogging {
    exceptionFormat = TestExceptionFormat.FULL
    showCauses = true
    showExceptions = true
    showStackTraces = true
    showStandardStreams = true
  }
}
