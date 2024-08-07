package nasa.gradle

import com.google.devtools.ksp.gradle.KspGradleSubplugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate

class ConventionHilt : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(KspGradleSubplugin::class.java)
    }

    val implementation by configurations
    val testImplementation by configurations
    val ksp by configurations

    dependencies {
      implementation(libs.hilt.core)
      ksp(libs.hilt.compiler)
      testImplementation(libs.test.hilt)
    }
  }
}
