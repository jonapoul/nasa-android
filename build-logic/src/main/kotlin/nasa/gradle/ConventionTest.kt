package nasa.gradle

import blueprint.core.intProperty
import com.android.build.gradle.api.AndroidBasePlugin
import kotlinx.kover.gradle.plugin.KoverGradlePlugin
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import kotlinx.kover.gradle.plugin.dsl.KoverReportsConfig
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
      apply("java-test-fixtures")
    }
    configureTesting()
    configurePowerAssert()
    configureKover()
  }

  private fun Project.configureTesting() {
    val testImplementation = configurations.findByName("testImplementation")
    val isAndroid = project.plugins.any { it is AndroidBasePlugin }

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

          val debugImplementation = configurations.getByName("debugImplementation")
          debugImplementation(libs.test.androidx.monitor)

          val androidTestImplementation = configurations.getByName("androidTestImplementation")
          androidTestImplementation(libs.test.androidx.arch)
          androidTestImplementation(libs.test.androidx.coreKtx)
          androidTestImplementation(libs.test.androidx.espresso)
          androidTestImplementation(libs.test.androidx.junit)
          androidTestImplementation(libs.test.androidx.rules)
          androidTestImplementation(libs.test.androidx.runner)
          androidTestImplementation(libs.test.timber)
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

  private fun Project.configureKover() {
    extensions.configure<KoverProjectExtension> {
      useJacoco = false
      reports { configureKoverReports(project) }
    }

    rootProject.dependencies {
      // Include this module in test coverage
      val kover = rootProject.configurations.getByName("kover")
      kover(project)
    }
  }
}

private fun KoverReportsConfig.configureKoverReports(project: Project) {
  total {
    filters {
      excludes {
        classes(
          "*Activity*",
          "*Application*",
          "*BuildConfig*",
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
          "dagger.Provides",
          "javax.annotation.processing.Generated",
        )
      }
    }

    log {
      onCheck = true
      coverageUnits = CoverageUnit.INSTRUCTION
      aggregationForGroup = AggregationType.COVERED_PERCENTAGE
    }
  }

  verify {
    rule {
      disabled = project != project.rootProject
      bound {
        minValue = project.intProperty(key = "blueprint.kover.minCoverage")
        coverageUnits = CoverageUnit.INSTRUCTION
        aggregationForGroup = AggregationType.COVERED_PERCENTAGE
      }
    }
  }
}
