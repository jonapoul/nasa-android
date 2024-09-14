package nasa.gradle

import com.autonomousapps.DependencyAnalysisPlugin
import com.squareup.sort.SortDependenciesPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper

class ModuleViewModel : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(KotlinAndroidPluginWrapper::class.java)
      apply(ConventionAndroidLibrary::class.java)
      apply(ConventionAndroidManagedDevices::class.java)
      apply(ConventionCompose::class.java)
      apply(ConventionDiagrams::class.java)
      apply(ConventionHilt::class.java)
      apply(ConventionStyle::class.java)
      apply(ConventionTest::class.java)
      apply(DependencyAnalysisPlugin::class.java)
      apply(SortDependenciesPlugin::class.java)
    }

    val api by configurations
    val implementation by configurations

    dependencies {
      api(libs.androidx.lifecycle.viewmodel.core)
      api(libs.dagger.core)
      implementation(libs.hilt.android)
      implementation(libs.molecule)
    }
  }
}
