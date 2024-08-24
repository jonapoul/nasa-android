package nasa.gradle

import blueprint.core.intProperty
import com.android.build.gradle.api.AndroidBasePlugin
import kotlinx.kover.gradle.plugin.KoverGradlePlugin
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.KoverReportExtension
import kotlinx.kover.gradle.plugin.dsl.MetricType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradlePlugin

class ConventionTest : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(KoverGradlePlugin::class.java)
      apply(PowerAssertGradlePlugin::class.java)
    }
    val isAndroid = project.plugins.any { it is AndroidBasePlugin }
    configureTesting(isAndroid)
    configurePowerAssert()
    configureKover(isAndroid)
  }

  private fun Project.configureTesting(isAndroid: Boolean) {
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
        testImplementation(project(":test:resources"))

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

    tasks.withType<Test> {
      if (name.contains("release", ignoreCase = true)) {
        enabled = false
      }

      testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
        showStandardStreams = true
      }
    }
  }

  @OptIn(ExperimentalKotlinGradlePluginApi::class)
  private fun Project.configurePowerAssert() {
    extensions.configure<PowerAssertGradleExtension> {
      functions = listOf(
        "kotlin.assert",
        "kotlin.test.assertEquals",
        "kotlin.test.assertFalse",
        "kotlin.test.assertIs",
        "kotlin.test.assertNotNull",
        "kotlin.test.assertNull",
        "kotlin.test.assertTrue",
      )
    }
  }

  private fun Project.configureKover(isAndroid: Boolean) {
    val isRootProject = project == rootProject
    val minCoverage = intProperty(key = "blueprint.kover.minCoverage")

    val androidVariant = when {
      isAndroid -> "debug"
      else -> null // kotlin JVM, no variant to merge with
    }

    extensions.configure<KoverReportExtension> { configure(isRootProject, androidVariant, minCoverage) }

    rootProject.dependencies {
      // Include this module in test coverage
      val kover = rootProject.configurations.getByName("kover")
      kover(project)
    }
  }

  private fun KoverReportExtension.configure(isRootProject: Boolean, androidVariant: String?, minCoverage: Int) {
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
            minValue = minCoverage
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
}
