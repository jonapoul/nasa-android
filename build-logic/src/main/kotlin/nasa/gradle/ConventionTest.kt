package nasa.gradle

import com.android.build.gradle.api.AndroidBasePlugin
import kotlinx.kover.gradle.plugin.KoverGradlePlugin
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
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(KoverGradlePlugin::class.java)
      apply(PowerAssertGradlePlugin::class.java)
      apply("java-test-fixtures")
    }
    configureTesting()
    configurePowerAssert()

    // Suppresses warning. See https://github.com/mockk/mockk/issues/1171
    tasks.withType<Test> {
      jvmArgs("-XX:+EnableDynamicAgentLoading")
    }
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
          debugImplementation(project(":test:hilt"))

          val androidTestImplementation = configurations.getByName("androidTestImplementation")
          androidTestImplementation(libs.test.alakazam.core)
          androidTestImplementation(libs.test.androidx.arch)
          androidTestImplementation(libs.test.androidx.coreKtx)
          androidTestImplementation(libs.test.androidx.espresso.core)
          androidTestImplementation(libs.test.androidx.espresso.intents)
          androidTestImplementation(libs.test.androidx.junit)
          androidTestImplementation(libs.test.androidx.navigation)
          androidTestImplementation(libs.test.androidx.rules)
          androidTestImplementation(libs.test.androidx.runner)
          androidTestImplementation(libs.test.junit)
          androidTestImplementation(libs.test.kotlin.common)
          androidTestImplementation(libs.test.kotlin.junit)
          androidTestImplementation(libs.test.kotlinx.coroutines)
          androidTestImplementation(libs.test.timber)
          androidTestImplementation(libs.test.turbine)
          androidTestImplementation(project(":test:resources"))
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
      functions = setOf(
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
}
