package nasa.gradle

import blueprint.recipes.androidBaseBlueprint
import blueprint.recipes.androidDesugaringBlueprint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate

class ConventionAndroidBase : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    androidBaseBlueprint()
    androidDesugaringBlueprint(libs.versions.android.desugaring)

    val implementation by configurations
    dependencies {
      implementation(libs.timber)
    }
  }
}
