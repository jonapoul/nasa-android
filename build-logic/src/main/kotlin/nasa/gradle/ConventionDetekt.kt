package nasa.gradle

import blueprint.recipes.detektBlueprint
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType

class ConventionDetekt : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    detektBlueprint()

    tasks.withType<Detekt>().configureEach {
      exclude { it.file.path.contains("generated") }
    }

    val detektPlugins by configurations
    dependencies {
      detektPlugins(libs.plugin.detektCompose)
    }
  }
}
