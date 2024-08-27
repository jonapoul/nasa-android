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

    val ksp by configurations
    val kspTest by configurations
    val testImplementation by configurations
    val androidTestImplementation by configurations

    dependencies {
      ksp(libs.hilt.compiler)
      kspTest(libs.hilt.compiler)

      testImplementation(libs.test.hilt)
      androidTestImplementation(libs.test.hilt)
    }
  }
}
