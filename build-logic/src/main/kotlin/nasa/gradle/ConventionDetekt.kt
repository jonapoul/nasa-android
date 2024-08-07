package nasa.gradle

import blueprint.recipes.detektBlueprint
import org.gradle.api.Plugin
import org.gradle.api.Project

class ConventionDetekt : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    detektBlueprint(composeDetektVersion = libs.versions.androidx.compose.lint.twitter)
  }
}
