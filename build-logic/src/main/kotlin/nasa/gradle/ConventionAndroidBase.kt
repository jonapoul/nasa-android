package nasa.gradle

import blueprint.recipes.androidBaseBlueprint
import blueprint.recipes.androidDesugaringBlueprint
import org.gradle.api.Plugin
import org.gradle.api.Project

class ConventionAndroidBase : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    androidBaseBlueprint()
    androidDesugaringBlueprint(libs.versions.android.desugaring)
  }
}
