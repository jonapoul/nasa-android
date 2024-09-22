package nasa.gradle

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

class ConventionCompose : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(ConventionKotlin::class.java)
      apply(ConventionAndroidBase::class.java)
      apply(ComposeCompilerGradleSubplugin::class.java)
    }

    extensions.findByType(CommonExtension::class)?.apply {
      buildFeatures {
        compose = true
      }
    }

    extensions.configure<ComposeCompilerGradlePluginExtension> {
      val metricReportDir = project.layout.buildDirectory.dir("compose_metrics").get().asFile
      metricsDestination = metricReportDir
      reportsDestination = metricReportDir
      stabilityConfigurationFile = rootProject.file("compose-stability.conf")
      targetKotlinPlatforms = setOf(KotlinPlatformType.androidJvm)
    }

    val androidTestImplementation by configurations
    val debugImplementation by configurations
    val implementation by configurations
    val lintChecks by configurations

    dependencies {
      implementation(platform(libs.androidx.compose.bom))
      lintChecks(libs.androidx.compose.lint)

      // Testing
      debugImplementation(libs.test.androidx.compose.ui.manifest)

      // UI testing
      androidTestImplementation(platform(libs.androidx.compose.bom))
      androidTestImplementation(libs.test.androidx.compose.ui.junit4)
    }
  }
}
