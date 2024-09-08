package nasa.gradle

import blueprint.recipes.detektBlueprint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate

class ConventionDetekt : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    detektBlueprint()

    val detektPlugins by configurations
    dependencies {
      detektPlugins(libs.plugin.detektCompose)
    }
  }
}
