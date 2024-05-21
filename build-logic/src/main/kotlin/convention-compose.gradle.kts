import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
  id("convention-kotlin")
  id("convention-android-base")
  id("org.jetbrains.kotlin.plugin.compose")
}

val libs = the<LibrariesForLibs>()

val ext: CommonExtension<*, *, *, *, *, *> = extensions.findByType<BaseAppModuleExtension>()
  ?: extensions.findByType<LibraryExtension>()
  ?: error("No android extension found in $path")

ext.apply {
  buildFeatures {
    compose = true
  }
}

composeCompiler {
  val metricReportDir = project.layout.buildDirectory.dir("compose_metrics").get().asFile
  metricsDestination = metricReportDir
  reportsDestination = metricReportDir
  stabilityConfigurationFile = rootProject.file("compose-stability.conf")
  targetKotlinPlatforms = setOf(KotlinPlatformType.androidJvm)
}

val implementation by configurations
val lintChecks by configurations

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  lintChecks(libs.androidx.compose.lint)
}
