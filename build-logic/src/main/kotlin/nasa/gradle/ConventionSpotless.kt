package nasa.gradle

import blueprint.recipes.spotlessBlueprint
import org.gradle.api.Plugin
import org.gradle.api.Project

class ConventionSpotless : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    spotlessBlueprint()
  }
}
