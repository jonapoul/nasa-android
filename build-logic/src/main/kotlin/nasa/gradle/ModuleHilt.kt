package nasa.gradle

import com.squareup.sort.SortDependenciesPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper

class ModuleHilt : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(KotlinAndroidPluginWrapper::class.java)
      apply(ConventionAndroidLibrary::class.java)
      apply(ConventionHilt::class.java)
      apply(ConventionStyle::class.java)
      apply(SortDependenciesPlugin::class.java)
    }

    val implementation by configurations

    dependencies {
      implementation(libs.hilt.core)
    }
  }
}
